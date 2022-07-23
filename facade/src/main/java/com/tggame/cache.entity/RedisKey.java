package com.tggame;

public enum RedisKey {
  BTC(60),
  ;

  private static final String preFix = "tggame";

  public static String getCachekey() {
    return preFix + "enum";
  }

  RedisKey(Integer time) {
    this.time = time;
  }

  public Integer time;

  /**
   * 获取投注游戏状态 key
   *
   * @param tgGroupId tg 群id
   * @param tgBotId tg 机器人id
   * @return key
   */
  public static String getBetStatusKey(String tgGroupId, String tgBotId) {
    return preFix + ":" + tgGroupId + ":" + tgBotId;
  }

  public static String genBTCKey(Long time) {
    return preFix + ":btc:" + time;
  }
}
