package com.tggame.events;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.tggame.open.entity.OpenEnum;
import com.tggame.open.entity.OpenRecord;
import com.tggame.trend.entity.TrendRecord;
import com.tggame.trend.service.TrendRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 订单监听器
 */
@Slf4j
@Component
public class TrendBuildEventListener {

    @Autowired
    private TrendRecordService trendRecordService;

    /**
     * 实现走势统计
     */
    @Async
    @EventListener(TrendBuildEvent.class)
    public void trendRecordCreate(TrendBuildEvent trendBuildEvent) {
        try {
            OpenRecord openRecord = trendBuildEvent.getOpenRecord();
            String[] resultArray = OpenEnum.Instance.drawn(openRecord.getNum());
            log.info("开奖结果-{}", resultArray);
            trendRecordService.save(TrendRecord.builder()
                    .lotteryId(openRecord.getLotteryId())
                    .issue(openRecord.getIssue())
                    .data(resultArray[0])
                    .openTime(DateUtil.format(new DateTime(), "HH:mm"))
                    .build());
        } catch (Exception e) {
            log.error("-{}", e.getLocalizedMessage(), e);
        }
    }


}

