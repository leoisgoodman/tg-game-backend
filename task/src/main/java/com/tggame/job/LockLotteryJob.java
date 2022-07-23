package com.tggame.job;

import com.tggame.service.OpenLotteryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 封盘调度
 */
@Slf4j
@Component
public class LockLotteryJob {

    @Autowired
    private OpenLotteryService openLotteryService;

    /**
     * 每期数分钟的第45秒执行
     */
    @Scheduled(cron = "45 1/2 * * * ? ")
    public void execute() throws Exception {
        log.info("开始封盘");
        openLotteryService.lock();
    }
}