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
import com.tggame.bet.vo.BetOrderSaveVO;
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
import com.tggame.user.entity.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
     * @param betOrderSaveVO 投注記錄
     */
    @Transactional
    @Override
    public void bet(BetOrderSaveVO betOrderSaveVO) {
        OpenRecord openRecord = openRecordDAO.selectOne(new LambdaQueryWrapper<OpenRecord>()
                .eq(OpenRecord::getStatus, OpenRecordStatus.Enable)
                .last("limit 1"));
        if (null == openRecord) {
            log.error("没有可投注期号错误-{}", betOrderSaveVO);
            throw new OpenRecordException(BaseException.BaseExceptionEnum.Bet_Lock);
        }
        //1.查詢個人賬戶餘額信息，並扣除餘額
        User user = userDAO.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getTgUserId, betOrderSaveVO.getTgUserId())
                .eq(User::getType, UserType.Player));
        if (null == user) {
            log.error("用戶不存在投注非法-{}", betOrderSaveVO);
            throw new UserException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }

        if(!CollectionUtils.isEmpty(betOrderSaveVO.getNumList())){
            if (user.getUsdtBalance() < betOrderSaveVO.getAmount() * betOrderSaveVO.getNumList().size()) {
                log.error("账户余额不足错误-{}", user);
                throw new UserException(BaseException.BaseExceptionEnum.Balance_Not_Enough);
            }
        }

        if (user.getUsdtBalance() < betOrderSaveVO.getAmount()) {
            log.error("账户余额不足错误-{}", user);
            throw new UserException(BaseException.BaseExceptionEnum.Balance_Not_Enough);
        }

        //2.查詢群信息，機器人信息
        Group group = groupDAO.selectOne(new LambdaQueryWrapper<Group>()
                .eq(Group::getId, user.getGroupId())
                .eq(Group::getTgGroupId, betOrderSaveVO.getTgGroupId())
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
                .eq(GroupBet::getCode, betOrderSaveVO.getBetCode().toLowerCase())
                .eq(GroupBet::getStatus, GroupBetStatus.Enable));
        if (null == groupBet) {
            log.error("投注项不存在错误-{}", betOrderSaveVO.getBetCode().toLowerCase());
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
                .eq(Bot::getTgBotId, betOrderSaveVO.getTgBotId())
                .eq(Bot::getStatus, BotStatus.Enable));
        if (null == bot) {
            log.error("机器人不存在错误-{}", betOrderSaveVO.getTgBotId());
            throw new BotException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }


        //4.保存投注記錄并扣除余额
        if(!CollectionUtils.isEmpty(betOrderSaveVO.getNumList())){
            user.setUsdtBalance(user.getUsdtBalance() - betOrderSaveVO.getAmount() * betOrderSaveVO.getNumList().size());
        }else{
            user.setUsdtBalance(user.getUsdtBalance() - betOrderSaveVO.getAmount());
        }
        user.setUpdateTime(new Date());
        userDAO.updateById(user);

        if(!CollectionUtils.isEmpty(betOrderSaveVO.getNumList())){
            List<BetOrder> betOrderList = new ArrayList<>();
            for(String num  : betOrderSaveVO.getNumList()){
                BetOrder betOrder = getBetOrder(betOrderSaveVO, openRecord, user, group, groupLottery, groupBet, lottery, bet, bot);
                betOrder.setBetNum(num);
                betOrder.setId(String.valueOf(uidGenerator.getUID()));
                betOrder.setStatus(BetOrderStatus.Bet.name());
                betOrder.setCreateTime(new Date());
                betOrder.setUpdateTime(new Date());
                betOrderList.add(betOrder);
            }
            super.saveBatch(betOrderList);
        }else{
            BetOrder betOrder = getBetOrder(betOrderSaveVO, openRecord, user, group, groupLottery, groupBet, lottery, bet, bot);
            this.save(betOrder);
        }
        //5.發佈投注事件

    }

    /**
     * 构建投注记录
     * @param betOrderSaveVO
     * @param openRecord
     * @param user
     * @param group
     * @param groupLottery
     * @param groupBet
     * @param lottery
     * @param bet
     * @param bot
     * @return
     */
    private BetOrder getBetOrder(BetOrderSaveVO betOrderSaveVO, OpenRecord openRecord, User user, Group group, GroupLottery groupLottery, GroupBet groupBet, Lottery lottery, Bet bet, Bot bot) {
        BetOrder betOrder = new BetOrder();
        BeanUtils.copyProperties(betOrderSaveVO,betOrder);
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
        betOrder.setBetCode(betOrder.getBetCode().toLowerCase());

        betOrder.setIssue(openRecord.getIssue());
        betOrder.setOpenId(openRecord.getId());
        Double shouldPayAmount = Double.parseDouble(new DecimalFormat("######0.00").format(betOrder.getAmount() * betOrder.getOdds() * betOrder.getPayBackPercent() / 100));
        betOrder.setShouldPayAmount(shouldPayAmount);
        Double winAmount = Double.parseDouble(new DecimalFormat("######0.00").format(betOrder.getAmount() * betOrder.getOdds()));
        betOrder.setWinAmount(winAmount);
        return betOrder;
    }
}


