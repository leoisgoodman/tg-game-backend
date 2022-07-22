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
     */
    @Scheduled(cron = "0 0/2 * * * ?")
    public void execute() {
        log.info("开始开奖");
        openLotteryService.open();
    }
}
