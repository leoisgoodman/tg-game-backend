/*
 * tg学习代码
 */
package com.tggame.user.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tggame.core.enums.YNEnum;
import com.tggame.user.dao.UserDAO;
import com.tggame.user.dao.mapper.UserMapper;
import com.tggame.user.entity.User;
import com.tggame.user.entity.UserStatus;
import com.tggame.user.entity.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * 群成员
 *
 * @author tg
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UidGenerator uidGenerator;

    @Transactional
    @Override
    public boolean save(User entity) {
        entity.setId(String.valueOf(uidGenerator.getUID()));
        entity.setUsername(entity.getTgUsername());
        entity.setStatus(UserStatus.Enable.name());
        entity.setIsAdmin(YNEnum.N.name());
        entity.setIsOwner(YNEnum.N.name());
        entity.setBetLostMoney(0L);
        entity.setBetTotalMoney(0L);
        entity.setBetWinMoney(0L);
        entity.setUsdtBalance(10000.00D);
        entity.setPercent(0);
        entity.setType(UserType.Demo.name());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        return super.save(entity);
    }

}


