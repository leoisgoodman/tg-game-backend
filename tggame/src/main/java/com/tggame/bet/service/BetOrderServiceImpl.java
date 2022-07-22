/*
 * tg学习代码
 */
package com.tggame.bet.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tggame.bet.dao.BetOrderDAO;
import com.tggame.bet.dao.mapper.BetOrderMapper;
import com.tggame.bet.entity.BetOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Date;


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
    private UidGenerator uidGenerator;

    @Transactional
    @Override
    public boolean save(BetOrder entity) {
//        entity.setId(String.valueOf(uidGenerator.getUID()));
        return super.save(entity);
    }

}


