/*
 * tg学习代码
 */
package com.tggame.bot.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tggame.bot.dao.BotDAO;
import com.tggame.bot.dao.mapper.BotMapper;
import com.tggame.bot.entity.Bot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * tg机器人
 *
 * @author tg
 */
@Slf4j
@Service
public class BotServiceImpl extends ServiceImpl<BotMapper, Bot> implements BotService {

    @Autowired
    private BotDAO botDAO;

    @Autowired
    private UidGenerator uidGenerator;

    @Transactional
    @Override
    public boolean save(Bot entity) {
//        entity.setId(String.valueOf(uidGenerator.getUID()));
        return super.save(entity);
    }

}


