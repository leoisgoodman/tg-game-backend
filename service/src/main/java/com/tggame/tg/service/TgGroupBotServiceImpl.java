/*
 * tg学习代码
 */
package com.tggame.tg.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tggame.tg.dao.TgGroupBotDAO;
import com.tggame.tg.dao.mapper.TgGroupBotMapper;
import com.tggame.tg.entity.TgGroupBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 群机器人
 *
 * @author tg
 */
@Slf4j
@Service
public class TgGroupBotServiceImpl extends ServiceImpl<TgGroupBotMapper, TgGroupBot> implements TgGroupBotService {

    @Autowired
    private TgGroupBotDAO tgGroupBotDAO;

    @Autowired
    private UidGenerator uidGenerator;

    @Transactional
    @Override
    public boolean save(TgGroupBot entity) {
//        entity.setId(String.valueOf(uidGenerator.getUID()));
        return super.save(entity);
    }

}


