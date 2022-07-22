package com.tggame.feign;

import com.tggame.feign.fallback.UidFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * uid api请求熔断器
 *
 * @author tg
 */
@FeignClient(name = "uid-api", fallback = UidFallback.class)
public interface UidClient {
    /**
     * 获取uid 唯一id（并发不重号）
     *
     * @return uid
     */
    @GetMapping(value = "/uid/load")
    Long uid();

}
