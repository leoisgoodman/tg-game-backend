/*
 * tg学习代码
 */
package com.tggame.trend.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tggame.trend.dao.TrendRecordDAO;
import com.tggame.trend.dao.mapper.TrendRecordMapper;
import com.tggame.trend.entity.TrendRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 走势记录
 *
 * @author tg
 */
@Slf4j
@Service
public class TrendRecordServiceImpl extends ServiceImpl<TrendRecordMapper, TrendRecord> implements TrendRecordService {

    @Autowired
    private TrendRecordDAO trendRecordDAO;

    @Autowired
    private UidGenerator uidGenerator;

    @Transactional
    @Override
    public boolean save(TrendRecord entity) {
//        entity.setId(String.valueOf(uidGenerator.getUID()));
        return super.save(entity);
    }

}


