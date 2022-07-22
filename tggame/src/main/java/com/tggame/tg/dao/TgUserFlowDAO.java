package com.tggame.tg.dao;

import com.tggame.tg.dao.mapper.TgUserFlowMapper;
import com.tggame.tg.entity.TgUserFlow;
import com.tggame.core.base.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * TgUserFlow DAO
 * 数据服务层
 *
 * @author tg
 */
@Repository
public class TgUserFlowDAO extends BaseDAO<TgUserFlowMapper, TgUserFlow> {


    /**
     * TgUserFlow mapper
     */
    @Autowired
    private TgUserFlowMapper tgUserFlowMapper;


}