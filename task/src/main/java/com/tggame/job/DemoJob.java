package com.tggame.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoJob {


    @Scheduled(cron = "* * * * * ?")
    public void execute() {
        log.info("===========>调度开始执行");
    }
}
