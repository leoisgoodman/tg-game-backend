package com.tggame.feign.fallback;

import com.tggame.feign.UidClient;
import org.springframework.stereotype.Component;

@Component
public class UidFallback implements UidClient {


    @Override
    public Long uid() {
        return null;
    }


}
