package com.tggame.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tggame.open.entity.OpenRecord;
import com.tggame.open.entity.OpenRecordStatus;
import com.tggame.open.service.OpenRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class OpenLotteryService {

    @Autowired
    private OpenRecordService openRecordService;

    /**
     * 实现开奖业务
     * 1.ws拉取奖源结果
     * 2.保存开奖记录
     * 3.开启下一期并生成新的一起进行ready状态
     */
    public void open() {
        //1.ws拉取奖源结果 todo
        String num = null;
        
        //2.更新开奖记录
        OpenRecord openRecord = openRecordService.getOne(new LambdaQueryWrapper<OpenRecord>()
                .eq(OpenRecord::getStatus, OpenRecordStatus.Enable)
                .orderByDesc(OpenRecord::getIssue)
                .last("limit 1"));

        openRecord.setNum(num);
        openRecord.setStatus(OpenRecordStatus.Drawn.name());
        openRecord.setUpdateTime(new Date());
        openRecordService.updateById(openRecord);

        //3.开启下一期并生成新的一起进行ready状态
        openRecordService.save(OpenRecord.builder()
                .build());
    }
}
