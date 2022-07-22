/*
 * tg学习代码
 */
package com.tggame.group.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tggame.group.dao.GroupBetDAO;
import com.tggame.group.dao.mapper.GroupBetMapper;
import com.tggame.group.entity.GroupBet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 群玩法
 *
 * @author tg
 */
@Slf4j
@Service
public class GroupBetServiceImpl extends ServiceImpl<GroupBetMapper, GroupBet> implements GroupBetService {

    @Autowired
    private GroupBetDAO groupBetDAO;

    @Autowired
    private UidGenerator uidGenerator;

    @Transactional
    @Override
    public boolean save(GroupBet entity) {
//        entity.setId(String.valueOf(uidGenerator.getUID()));
        return super.save(entity);
    }

}


