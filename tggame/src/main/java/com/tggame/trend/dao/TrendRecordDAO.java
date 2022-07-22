package com.tggame.trend.dao;

import com.tggame.trend.dao.mapper.TrendRecordMapper;
import com.tggame.trend.entity.TrendRecord;
import com.tggame.core.base.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * TrendRecord DAO
 * 数据服务层
 *
 * @author tg
 */
@Repository
public class TrendRecordDAO extends BaseDAO<TrendRecordMapper, TrendRecord> {


    /**
     * TrendRecord mapper
     */
    @Autowired
    private TrendRecordMapper trendRecordMapper;


}