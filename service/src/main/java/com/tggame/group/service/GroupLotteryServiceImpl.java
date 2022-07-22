/*
 * tg学习代码
 */
package com.tggame.group.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tggame.group.dao.GroupLotteryDAO;
import com.tggame.group.dao.mapper.GroupLotteryMapper;
import com.tggame.group.entity.GroupLottery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 群彩种
 *
 * @author tg
 */
@Slf4j
@Service
public class GroupLotteryServiceImpl extends ServiceImpl<GroupLotteryMapper, GroupLottery> implements GroupLotteryService {

    @Autowired
    private GroupLotteryDAO groupLotteryDAO;

    @Autowired
    private UidGenerator uidGenerator;

    @Transactional
    @Override
    public boolean save(GroupLottery entity) {
//        entity.setId(String.valueOf(uidGenerator.getUID()));
        return super.save(entity);
    }

}


