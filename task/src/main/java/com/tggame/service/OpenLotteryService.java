package com.tggame.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tggame.RedisKey;
import com.tggame.cache.service.RedisServiceSVImpl;
import com.tggame.events.TrendBuildEvent;
import com.tggame.open.entity.OpenRecord;
import com.tggame.open.entity.OpenRecordStatus;
import com.tggame.open.service.OpenRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class OpenLotteryService {

    @Autowired
    private OpenRecordService openRecordService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private RedisServiceSVImpl redisServiceSV;

    /**
     * 实现开奖业务
     * 1.ws拉取奖源结果
     * 2.保存开奖记录
     * 3.开启下一期并生成新的一起进行ready状态
     * 4.统计走势
     */
    public void open() throws Exception {
        // 1.ws拉取奖源结果
        Long time =
                new DateTime(DateUtil.truncate(DateUtil.calendar(new Date()), DateField.MINUTE)).getTime();
        String btcKey = RedisKey.genBTCKey(time);
        if (!redisServiceSV.hasKey(btcKey)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new Exception(e);
            }
        }
        String num = (String) redisServiceSV.get(btcKey);
        log.info("获取到的BTC的价格为:{}",num);


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

        //4.统计走势
//        applicationEventPublisher.publishEvent(new TrendBuildEvent(this, openRecord));
    }
}
