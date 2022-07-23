package com.tggame.config;

import com.tggame.ws.BTCWebSocketClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(value = 100)
@Slf4j
public class WebSocketClient implements CommandLineRunner {
  private static String url = "wss://stream.binance.com/stream";

  private static String request =
      "{\"method\":\"SUBSCRIBE\",\"params\":[\"btcusdt@kline_1m\"],\"id\":1}";

  @Override
  public void run(String... args) throws Exception {
    try {
      BTCWebSocketClient myClient = new BTCWebSocketClient();
      myClient.connect();
      // 判断是否连接成功，未成功后面发送消息时会报错
      while (!myClient.getReadyState().equals(org.java_websocket.WebSocket.READYSTATE.OPEN)) {
        log.info("连接中···请稍后");
        Thread.sleep(100);
      }
      myClient.send(request);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
