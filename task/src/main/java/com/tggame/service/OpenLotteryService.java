package com.tggame.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tggame.RedisKey;
import com.tggame.cache.service.RedisServiceSVImpl;
import com.tggame.events.BetOrderDrawnEvent;
import com.tggame.events.TrendBuildEvent;
import com.tggame.open.entity.OpenRecord;
import com.tggame.open.entity.OpenRecordStatus;
import com.tggame.open.service.OpenRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 实现开奖业务 1.ws拉取奖源结果 2.保存开奖记录 3.开启下一期并生成新的一起进行ready状态 4.统计走势
     */
    @Transactional(rollbackFor = Exception.class)
    public void open() {
        // 1.ws拉取奖源结果
        Long time = new DateTime(DateUtil.truncate(DateUtil.calendar(new Date()), DateField.MINUTE)).getTime();

        String btcKey = RedisKey.genBTCKey(time);
        String num = (String) redisServiceSV.get(btcKey);
        if (StringUtils.isBlank(num)) {
            return;
        }
        redisServiceSV.del(btcKey);

        log.info("获取到的BTC的价格为:{}", num);

        // 2.更新开奖记录  状态是为开奖
        OpenRecord openRecord = openRecordService.getOne(new LambdaQueryWrapper<OpenRecord>()
                .eq(OpenRecord::getStatus, OpenRecordStatus.Lock)
                .orderByDesc(OpenRecord::getIssue)
                .last("limit 1"));
        if (null == openRecord) {
            return;
        }
        openRecord.setNum(num);
        openRecord.setStatus(OpenRecordStatus.Drawn.name());
        openRecord.setUpdateTime(new Date());
        openRecordService.updateById(openRecord);

        // 3.保存下一期
        String openTimeStr = DateUtil.format(new DateTime(), "HH:mm");
        long issue = Long.parseLong(DateUtil.format(DateUtil.offsetMinute(new Date(), 2), DatePattern.PURE_DATETIME_PATTERN).substring(0, 12));
        String openTime = issue % 2 == 0 ? openTimeStr : DateUtil.format(DateUtil.offsetMinute(new Date(), 1), "HH:mm");
        openRecordService.save(OpenRecord.builder()
                .issue(issue % 2 == 0 ? issue : issue + 1)
                .openTime(openTime)
                .status(OpenRecordStatus.Ready.name())
                .build());


        // 4.统计走势,派獎等事件發佈
        applicationEventPublisher.publishEvent(new TrendBuildEvent(this, openRecord));
        applicationEventPublisher.publishEvent(new BetOrderDrawnEvent(this, openRecord));
    }

    @Transactional(rollbackFor = Exception.class)
    public void lock() {
        // 查询最新的可投注数据
        OpenRecord openRecord = openRecordService.getOne(new LambdaQueryWrapper<OpenRecord>()
                .eq(OpenRecord::getStatus, OpenRecordStatus.Enable)
                .orderByDesc(OpenRecord::getIssue)
                .last("limit 1"));

        openRecord.setStatus(OpenRecordStatus.Lock.name());
        openRecord.setUpdateTime(new Date());
        openRecordService.updateById(openRecord);

        //开启下一期并生成新的一起进行Enable状态
        OpenRecord nextOpenRecord = openRecordService.getOne(new LambdaQueryWrapper<OpenRecord>()
                .eq(OpenRecord::getStatus, OpenRecordStatus.Ready)
                .orderByDesc(OpenRecord::getIssue)
                .last("limit 1"));
        nextOpenRecord.setStatus(OpenRecordStatus.Enable.name());
        nextOpenRecord.setUpdateTime(new Date());
        openRecordService.updateById(nextOpenRecord);
    }
}
