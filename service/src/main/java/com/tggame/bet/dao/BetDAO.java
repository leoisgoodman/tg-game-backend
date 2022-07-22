package com.tggame.bet.dao;

import com.tggame.bet.dao.mapper.BetMapper;
import com.tggame.bet.entity.Bet;
import com.tggame.core.base.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Bet DAO
 * 数据服务层
 *
 * @author tg
 */
@Repository
public class BetDAO extends BaseDAO<BetMapper, Bet> {


    /**
     * Bet mapper
     */
    @Autowired
    private BetMapper betMapper;


}