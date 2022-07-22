package com.tggame.lottery.dao;

import com.tggame.core.base.BaseDAO;
import com.tggame.lottery.dao.mapper.LotteryMapper;
import com.tggame.lottery.entity.Lottery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Lottery DAO
 * 数据服务层
 *
 * @author tg
 */
@Repository
public class LotteryDAO extends BaseDAO<LotteryMapper, Lottery> {


    /**
     * Lottery mapper
     */
    @Autowired
    private LotteryMapper lotteryMapper;


}