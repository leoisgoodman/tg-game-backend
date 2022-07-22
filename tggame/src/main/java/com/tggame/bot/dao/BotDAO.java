package com.tggame.bot.dao;

import com.tggame.bot.dao.mapper.BotMapper;
import com.tggame.bot.entity.Bot;
import com.tggame.core.base.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Bot DAO
 * 数据服务层
 *
 * @author tg
 */
@Repository
public class BotDAO extends BaseDAO<BotMapper, Bot> {


    /**
     * Bot mapper
     */
    @Autowired
    private BotMapper botMapper;


}