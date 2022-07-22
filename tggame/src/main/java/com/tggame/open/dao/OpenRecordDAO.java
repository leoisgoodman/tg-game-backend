package com.tggame.open.dao;

import com.tggame.open.dao.mapper.OpenRecordMapper;
import com.tggame.open.entity.OpenRecord;
import com.tggame.core.base.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * OpenRecord DAO
 * 数据服务层
 *
 * @author tg
 */
@Repository
public class OpenRecordDAO extends BaseDAO<OpenRecordMapper, OpenRecord> {


    /**
     * OpenRecord mapper
     */
    @Autowired
    private OpenRecordMapper openRecordMapper;


}