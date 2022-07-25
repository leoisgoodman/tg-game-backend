package com.tggame.events;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
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

            //计算大小单双
            String[] arr = openRecord.getNum().replace(".", "").split("");
            int sum = 0;
            for (String s : arr) {
                sum += Integer.parseInt(s);
            }

//            String lastNum = openRecord.getNum().split("\\.")[1];
//            String bigSmall = Integer.parseInt(lastNum) < 5 ? "小" : "大";
//            String oddEven = Integer.parseInt(lastNum) % 2 == 0 ? "双" : "单";
//            String data = lastNum + "|" + bigSmall + "|" + oddEven;
            trendRecordService.save(TrendRecord.builder()
                    .lotteryId(openRecord.getLotteryId())
                    .issue(openRecord.getIssue())
                    .data(String.valueOf(sum))
                    .openTime(DateUtil.format(new DateTime(), "HH:mm"))
                    .build());
        } catch (Exception e) {
            log.error("-{}", e.getLocalizedMessage(), e);
        }
    }


}

