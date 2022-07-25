package com.tggame.events;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.tggame.bet.entity.BetCode;
import com.tggame.bet.entity.BetOrder;
import com.tggame.bet.entity.BetOrderBetType;
import com.tggame.bet.entity.BetOrderStatus;
import com.tggame.bet.service.BetOrderService;
import com.tggame.open.entity.OpenRecord;
import com.tggame.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

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
     */
    @Async
    @EventListener(BetOrderDrawnEvent.class)
    public void betOrderDrawn(BetOrderDrawnEvent betOrderDrawnEvent) {
        OpenRecord openRecord = betOrderDrawnEvent.getOpenRecord();
        int num = Integer.parseInt(openRecord.getNum().split("\\.")[1]);
        BetCode bigSmallBetCode = num >= 5 ? BetCode.Big : BetCode.Small;
        BetCode oddEvenBetCode = num % 2 == 0 ? BetCode.Odd : BetCode.Even;

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
    }

}

