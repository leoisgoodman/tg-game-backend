package com.tggame.tg.dao;

import com.tggame.tg.dao.mapper.TgGroupBotMapper;
import com.tggame.tg.entity.TgGroupBot;
import com.tggame.core.base.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * TgGroupBot DAO
 * 数据服务层
 *
 * @author tg
 */
@Repository
public class TgGroupBotDAO extends BaseDAO<TgGroupBotMapper, TgGroupBot> {


    /**
     * TgGroupBot mapper
     */
    @Autowired
    private TgGroupBotMapper tgGroupBotMapper;


}