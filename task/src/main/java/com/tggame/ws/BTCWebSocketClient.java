package com.tggame.ws;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.tggame.RedisKey;
import com.tggame.cache.service.RedisServiceSVImpl;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Component
public class BTCWebSocketClient extends WebSocketClient {

    private static String url = "wss://stream.binance.com/stream";

    /**
     * 此处是解决无法注入的关键
     */
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        BTCWebSocketClient.applicationContext = applicationContext;
    }

    public BTCWebSocketClient() throws URISyntaxException {
        super(new URI(url));
    }

    @Override
    public void onOpen(ServerHandshake shake) {
        log.info("握手...");
    }

    @Override
    public void onMessage(String paramString) {
        log.info("收到的消息为{}", paramString);
        this.processMessage(paramString);
    }

    @Override
    public void onClose(int paramInt, String paramString, boolean paramBoolean) {
        log.info("关闭...");
        // todo  要重试
    }

    @Override
    public void onError(Exception e) {
        log.info("异常...");

        //// todo  要重试
    }

    /**
     * 私有方法 收到消息后的处理
     *
     * @param paramString
     */
    private void processMessage(String paramString) {
        if (paramString.contains("btcusdt@kline_1m")) {
            JSONObject obj = JSONObject.parseObject(paramString);
            String data = obj.getString("data");
            JSONObject objData = JSONObject.parseObject(data);
            String dataK = objData.getString("k");
            JSONObject objDataK = JSONObject.parseObject(dataK);
            Long beginTime = objDataK.getLong("t");
            Integer minute = DateUtil.minute(DateUtil.date(beginTime));
            if (minute % 2 == 1) {
                return;
            }
            String btcKey = RedisKey.genBTCKey(beginTime);
            RedisServiceSVImpl redisServiceSV = applicationContext.getBean(RedisServiceSVImpl.class);
            if (redisServiceSV.hasKey(btcKey)) {
                return;
            }
            String btcValue = objDataK.getString("o");
            String value = btcValue.substring(0, btcValue.length() - 1);
            redisServiceSV.set(btcKey, value, RedisKey.BTC.time);
            log.debug("redis的key:{},值为:{}", btcKey, value);
        }
    }

}
