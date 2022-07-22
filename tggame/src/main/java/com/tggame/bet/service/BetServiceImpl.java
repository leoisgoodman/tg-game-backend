/*
 * tg学习代码
 */
package com.tggame.bet.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tggame.bet.dao.BetDAO;
import com.tggame.bet.dao.mapper.BetMapper;
import com.tggame.bet.entity.Bet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Date;


/**
 * 玩法
 *
 * @author tg
 */
@Slf4j
@Service
public class BetServiceImpl extends ServiceImpl<BetMapper, Bet> implements BetService {

    @Autowired
    private BetDAO betDAO;

    @Autowired
    private UidGenerator uidGenerator;

    @Transactional
    @Override
    public boolean save(Bet entity) {
//        entity.setId(String.valueOf(uidGenerator.getUID()));
        return super.save(entity);
    }

}


