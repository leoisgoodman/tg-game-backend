package com.tggame.group.dao;

import com.tggame.group.dao.mapper.GroupMapper;
import com.tggame.group.entity.Group;
import com.tggame.core.base.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Group DAO
 * 数据服务层
 *
 * @author tg
 */
@Repository
public class GroupDAO extends BaseDAO<GroupMapper, Group> {


    /**
     * Group mapper
     */
    @Autowired
    private GroupMapper groupMapper;


}