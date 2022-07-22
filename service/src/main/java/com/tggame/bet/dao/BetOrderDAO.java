package com.tggame.bet.dao;

import com.tggame.bet.dao.mapper.BetOrderMapper;
import com.tggame.bet.entity.BetOrder;
import com.tggame.core.base.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * BetOrder DAO
 * 数据服务层
 *
 * @author tg
 */
@Repository
public class BetOrderDAO extends BaseDAO<BetOrderMapper, BetOrder> {


    /**
     * BetOrder mapper
     */
    @Autowired
    private BetOrderMapper betOrderMapper;


}