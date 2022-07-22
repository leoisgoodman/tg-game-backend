package com.tggame.group.dao;

import com.tggame.core.base.BaseDAO;
import com.tggame.group.dao.mapper.GroupBetMapper;
import com.tggame.group.entity.GroupBet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * GroupBet DAO
 * 数据服务层
 *
 * @author tg
 */
@Repository
public class GroupBetDAO extends BaseDAO<GroupBetMapper, GroupBet> {


    /**
     * GroupBet mapper
     */
    @Autowired
    private GroupBetMapper groupBetMapper;


}