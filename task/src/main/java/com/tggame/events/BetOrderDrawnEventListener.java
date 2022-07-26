package com.tggame.events;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tggame.bet.entity.BetCode;
import com.tggame.bet.entity.BetOrder;
import com.tggame.bet.entity.BetOrderBetType;
import com.tggame.bet.entity.BetOrderStatus;
import com.tggame.bet.service.BetOrderService;
import com.tggame.core.base.BaseException;
import com.tggame.exceptions.UserException;
import com.tggame.open.entity.OpenEnum;
import com.tggame.open.entity.OpenRecord;
import com.tggame.user.entity.User;
import com.tggame.user.entity.UserStatus;
import com.tggame.user.entity.UserType;
import com.tggame.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单监听器
 */
@Slf4j
@Component
public class BetOrderDrawnEventListener {

    @Autowired
    private BetOrderService betOrderService;

    @Autowired
    private UserService userService;

    /**
     * 开奖业务
     * 1.更新中间注单
     * 2.为用户派彩
     * 3.庄家的收入变动
     */
    @Async
    @EventListener(BetOrderDrawnEvent.class)
    public void betOrderDrawn(BetOrderDrawnEvent betOrderDrawnEvent) {
        OpenRecord openRecord = betOrderDrawnEvent.getOpenRecord();
        String[] arr = OpenEnum.Instance.drawn(openRecord.getNum());
        BetCode bigSmallBetCode = Integer.parseInt(arr[0]) >= 5 ? BetCode.Big : BetCode.Small;
        BetCode oddEvenBetCode = Integer.parseInt(arr[0]) % 2 == 0 ? BetCode.Even : BetCode.Odd;
        String num = arr[0];

        // 1.更新中獎注单
        betOrderService.update(new LambdaUpdateWrapper<BetOrder>()
                .set(BetOrder::getStatus, BetOrderStatus.Win)
                .set(BetOrder::getUpdateTime, new Date())
                .eq(BetOrder::getIssue, openRecord.getIssue())
                .eq(BetOrder::getOpenId, openRecord.getId())
                .eq(BetOrder::getStatus, BetOrderStatus.Bet)
                .eq(BetOrder::getBetType, BetOrderBetType.Both_Sides)
                .in(BetOrder::getBetCode, bigSmallBetCode.val, oddEvenBetCode.val));

        betOrderService.update(new LambdaUpdateWrapper<BetOrder>()
                .set(BetOrder::getStatus, BetOrderStatus.Lost)
                .set(BetOrder::getUpdateTime, new Date())
                .eq(BetOrder::getIssue, openRecord.getIssue())
                .eq(BetOrder::getOpenId, openRecord.getId())
                .eq(BetOrder::getStatus, BetOrderStatus.Bet)
                .eq(BetOrder::getBetType, BetOrderBetType.Both_Sides)
                .notIn(BetOrder::getBetCode, bigSmallBetCode.val, oddEvenBetCode.val));

        betOrderService.update(new LambdaUpdateWrapper<BetOrder>()
                .set(BetOrder::getStatus, BetOrderStatus.Win)
                .set(BetOrder::getUpdateTime, new Date())
                .eq(BetOrder::getIssue, openRecord.getIssue())
                .eq(BetOrder::getOpenId, openRecord.getId())
                .eq(BetOrder::getStatus, BetOrderStatus.Bet)
                .eq(BetOrder::getBetType, BetOrderBetType.Num)
                .in(BetOrder::getBetNum, num));

        betOrderService.update(new LambdaUpdateWrapper<BetOrder>()
                .set(BetOrder::getStatus, BetOrderStatus.Lost)
                .set(BetOrder::getUpdateTime, new Date())
                .eq(BetOrder::getIssue, openRecord.getIssue())
                .eq(BetOrder::getOpenId, openRecord.getId())
                .eq(BetOrder::getStatus, BetOrderStatus.Bet)
                .eq(BetOrder::getBetType, BetOrderBetType.Num)
                .notIn(BetOrder::getBetNum, num));

        // 2.为用户派彩
        List<BetOrder> betOrderList = betOrderService.list(new LambdaQueryWrapper<BetOrder>()
                .eq(BetOrder::getOpenId, openRecord.getId())
                .eq(BetOrder::getIssue, openRecord.getIssue())
                .eq(BetOrder::getStatus, BetOrderStatus.Win));
        if (CollectionUtils.isEmpty(betOrderList)) {
            return;
        }
        //玩家派彩总额
        Double winTotal = CollectionUtils.isNotEmpty(betOrderList) ? betOrderList.stream().mapToDouble(BetOrder::getAmount).sum() : 0;
        log.info("玩家需要派彩总额-{}", winTotal);

        if (CollectionUtils.isNotEmpty(betOrderList)) {

            List<User> userList = userService.list(new LambdaQueryWrapper<User>()
                    .in(User::getId, betOrderList.stream()
                            .map(betOrder -> betOrder.getUserId())
                            .collect(Collectors.toSet())));
            //批量派彩給用戶
            for (BetOrder betOrder : betOrderList) {
                for (User user : userList) {
                    if (user.getId().equals(betOrder.getUserId())) {
                        user.setUsdtBalance(user.getUsdtBalance() + betOrder.getShouldPayAmount());
                        user.setUpdateTime(new Date());
                    }
                }
            }

            log.info("批量派彩操作-{}", userList);
            userService.updateBatchById(userList);
        }

        //3.庄家的收入变动
        this.bankerAmount(openRecord, winTotal);
    }

    /**
     * 庄家扣款和上分
     */
    private void bankerAmount(OpenRecord openRecord, Double winTotal) {
        List<BetOrder> betOrderLostList = betOrderService.list(new LambdaQueryWrapper<BetOrder>()
                .select(BetOrder::getBetId, BetOrder::getAmount)
                .eq(BetOrder::getOpenId, openRecord.getId())
                .eq(BetOrder::getIssue, openRecord.getIssue())
                .eq(BetOrder::getStatus, BetOrderStatus.Lost));

        Double incomeTotal = CollectionUtils.isNotEmpty(betOrderLostList) ? betOrderLostList.stream().mapToDouble(BetOrder::getAmount).sum() : 0;

        //最终庄家盈利
        Double totalAmount = incomeTotal - winTotal;

        if (0 == totalAmount) {
            log.warn("盈亏互抵，暂不用调整庄家余额-{}-{}", incomeTotal, -winTotal);
            return;
        }

        List<User> userList = userService.list(new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsdtBalance, User::getPercent)
                .eq(User::getStatus, UserStatus.Enable)
                .eq(User::getType, UserType.Banker));
        if (CollectionUtils.isEmpty(userList)) {
            throw new UserException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }

        for (User user : userList) {
            String usdtBalance = new DecimalFormat("######0.00").format(user.getUsdtBalance() + user.getPercent() / 100 * totalAmount);
            user.setUsdtBalance(Double.parseDouble(usdtBalance));
        }

        userService.updateBatchById(userList);
    }

}

