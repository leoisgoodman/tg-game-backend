package com.tggame.user.dao;

import com.tggame.core.base.BaseDAO;
import com.tggame.user.dao.mapper.UserMapper;
import com.tggame.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * User DAO
 * 数据服务层
 *
 * @author tg
 */
@Repository
public class UserDAO extends BaseDAO<UserMapper, User> {


    /**
     * User mapper
     */
    @Autowired
    private UserMapper userMapper;


}