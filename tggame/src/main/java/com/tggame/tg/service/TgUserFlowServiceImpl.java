/*
 * tg学习代码
 */
package com.tggame.tg.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tggame.tg.dao.TgUserFlowDAO;
import com.tggame.tg.dao.mapper.TgUserFlowMapper;
import com.tggame.tg.entity.TgUserFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Date;


/**
 * 用户流水
 *
 * @author tg
 */
@Slf4j
@Service
public class TgUserFlowServiceImpl extends ServiceImpl<TgUserFlowMapper, TgUserFlow> implements TgUserFlowService {

    @Autowired
    private TgUserFlowDAO tgUserFlowDAO;

    @Autowired
    private UidGenerator uidGenerator;

    @Transactional
    @Override
    public boolean save(TgUserFlow entity) {
//        entity.setId(String.valueOf(uidGenerator.getUID()));
        return super.save(entity);
    }

}


