/*
 * tg学习代码
 */
package com.tggame.bet.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tggame.bet.dao.BetDAO;
import com.tggame.bet.dao.BetOrderDAO;
import com.tggame.bet.dao.mapper.BetOrderMapper;
import com.tggame.bet.entity.Bet;
import com.tggame.bet.entity.BetOrder;
import com.tggame.bet.entity.BetOrderStatus;
import com.tggame.bot.dao.BotDAO;
import com.tggame.bot.entity.Bot;
import com.tggame.bot.entity.BotStatus;
import com.tggame.core.base.BaseException;
import com.tggame.exceptions.*;
import com.tggame.group.dao.GroupBetDAO;
import com.tggame.group.dao.GroupDAO;
import com.tggame.group.dao.GroupLotteryDAO;
import com.tggame.group.entity.*;
import com.tggame.lottery.dao.LotteryDAO;
import com.tggame.lottery.entity.Lottery;
import com.tggame.open.dao.OpenRecordDAO;
import com.tggame.open.entity.OpenRecord;
import com.tggame.open.entity.OpenRecordStatus;
import com.tggame.user.dao.UserDAO;
import com.tggame.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Locale;


/**
 * 投注
 *
 * @author tg
 */
@Slf4j
@Service
public class BetOrderServiceImpl extends ServiceImpl<BetOrderMapper, BetOrder> implements BetOrderService {

    @Autowired
    private BetOrderDAO betOrderDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private GroupLotteryDAO groupLotteryDAO;

    @Autowired
    private GroupBetDAO groupBetDAO;

    @Autowired
    private GroupDAO groupDAO;

    @Autowired
    private LotteryDAO lotteryDAO;

    @Autowired
    private BotDAO botDAO;

    @Autowired
    private BetDAO betDAO;

    @Autowired
    private OpenRecordDAO openRecordDAO;

    @Autowired
    private UidGenerator uidGenerator;

    @Transactional
    @Override
    public boolean save(BetOrder entity) {
        entity.setId(String.valueOf(uidGenerator.getUID()));
        entity.setStatus(BetOrderStatus.Bet.name());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        return super.save(entity);
    }

    /**
     * 投注
     * 1.查詢個人賬戶餘額信息，並扣除餘額
     * 2.查詢群信息，機器人信息
     * 3.查詢彩票信息
     * 4.保存投注記錄并扣除余额
     * 5.發佈投注事件
     *
     * @param betOrder 投注記錄
     * @param betCode  投注編碼
     */
    @Transactional
    @Override
    public void bet(BetOrder betOrder, String betCode) {
        OpenRecord openRecord = openRecordDAO.selectOne(new LambdaQueryWrapper<OpenRecord>()
                .eq(OpenRecord::getStatus, OpenRecordStatus.Enable));
        if (null == openRecord) {
            log.error("没有可投注期号错误-{}-{}", betOrder, betCode);
            throw new OpenRecordException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }
        //1.查詢個人賬戶餘額信息，並扣除餘額
        User user = userDAO.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getTgUserId, betOrder.getTgUserId()));
        if (null == user) {
            log.error("用戶不存在投注非法-{}", betOrder);
            throw new UserException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }

        if (user.getUsdtBalance() < betOrder.getAmount()) {
            log.error("账户余额不足错误-{}", user);
            throw new UserException(BaseException.BaseExceptionEnum.Balance_Not_Enough);
        }

        //2.查詢群信息，機器人信息
        Group group = groupDAO.selectOne(new LambdaQueryWrapper<Group>()
                .eq(Group::getId, user.getGroupId())
                .eq(Group::getTgGroupId, betOrder.getTgGroupId())
                .eq(Group::getStatus, GroupStatus.Enable));
        if (null == group) {
            log.error("群不存在，或不可用错误-{}", user.getGroupId());
            throw new GroupException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }

        //3.查詢彩票信息
        GroupLottery groupLottery = groupLotteryDAO.selectOne(new LambdaQueryWrapper<GroupLottery>()
                .eq(GroupLottery::getGroupId, group.getId()));
        if (null == groupLottery) {
            log.error("群彩种不存在错误-{}", group);
            throw new GroupLotteryException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }

        GroupBet groupBet = groupBetDAO.selectOne(new LambdaQueryWrapper<GroupBet>()
                .eq(GroupBet::getGroupId, group.getId())
                .eq(GroupBet::getGroupLotteryId, groupLottery.getLotteryId())
                .eq(GroupBet::getCode, betCode.toLowerCase(Locale.ROOT))
                .eq(GroupBet::getStatus, GroupBetStatus.Enable));
        if (null == groupBet) {
            log.error("投注项不存在错误-{}", betCode);
            throw new GroupBetException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }

        Lottery lottery = lotteryDAO.selectById(groupLottery.getLotteryId());
        if (null == lottery) {
            log.error("彩种不存在错误-{}", groupLottery);
            throw new LotteryException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }

        Bet bet = betDAO.selectById(groupBet.getBetId());
        if (null == bet) {
            log.error("投注玩法不存在错误-{}", groupBet);
            throw new BetException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }

        Bot bot = botDAO.selectOne(new LambdaQueryWrapper<Bot>()
                .eq(Bot::getTgBotId, betOrder.getTgBotId())
                .eq(Bot::getStatus, BotStatus.Enable));
        if (null == bot) {
            log.error("机器人不存在错误-{}", betOrder.getTgBotId());
            throw new BotException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }


        //4.保存投注記錄并扣除余额
        user.setUsdtBalance(user.getUsdtBalance() - betOrder.getAmount());
        user.setUpdateTime(new Date());
        userDAO.updateById(user);

        betOrder.setBetType(groupBet.getType());
        betOrder.setBetId(groupBet.getBetId());
        betOrder.setBetName(bet.getName());
        betOrder.setLotteryId(groupLottery.getLotteryId());
        betOrder.setLotteryName(lottery.getName());
        betOrder.setGroupId(group.getId());
        betOrder.setBotId(bot.getId());
        betOrder.setUserId(user.getId());
        betOrder.setOdds(groupBet.getOdds());
        betOrder.setPayBackPercent(groupBet.getPayBackPercent());

        betOrder.setIssue(openRecord.getIssue());
        betOrder.setOpenId(openRecord.getId());

        betOrder.setShouldPayAmount(betOrder.getAmount() * betOrder.getOdds() * betOrder.getPayBackPercent());
        betOrder.setWinAmount(betOrder.getAmount() * betOrder.getOdds());

        this.save(betOrder);

        //5.發佈投注事件

    }
}


