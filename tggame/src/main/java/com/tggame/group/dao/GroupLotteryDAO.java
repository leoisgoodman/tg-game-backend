package com.tggame.group.dao;

import com.tggame.group.dao.mapper.GroupLotteryMapper;
import com.tggame.group.entity.GroupLottery;
import com.tggame.core.base.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * GroupLottery DAO
 * 数据服务层
 *
 * @author tg
 */
@Repository
public class GroupLotteryDAO extends BaseDAO<GroupLotteryMapper, GroupLottery> {


    /**
     * GroupLottery mapper
     */
    @Autowired
    private GroupLotteryMapper groupLotteryMapper;


}