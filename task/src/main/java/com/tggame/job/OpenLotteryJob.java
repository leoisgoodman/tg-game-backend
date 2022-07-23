package com.tggame.job;

import com.tggame.service.OpenLotteryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 开奖业务job
 */
@Slf4j
@Component
public class OpenLotteryJob {

    @Autowired
    private OpenLotteryService openLotteryService;

    /**
     * 每2分钟查询一次奖源，进行开奖
     * 执行周期
     * 2022-07-23 20:14:58
     * 2022-07-23 20:14:59
     * 2022-07-23 20:16:00
     * 2022-07-23 20:16:01
     * 2022-07-23 20:16:02
     * 2022-07-23 20:16:03
     * 2022-07-23 20:16:58
     * 2022-07-23 20:16:59
     * 2022-07-23 20:18:00
     * 2022-07-23 20:18:01
     */
    @Scheduled(cron = "0,1,2,3,58,59 0/2 * * * ?")
    public void execute() throws Exception {
        log.info("开始开奖");
        openLotteryService.open();
    }
}
