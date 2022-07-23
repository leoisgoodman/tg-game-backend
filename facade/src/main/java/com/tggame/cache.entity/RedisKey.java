package com.tggame;


public enum RedisKey {
    ,
    ;

    private static final String preFix = "tggame";

    public static String getCachekey() {
        return preFix + "enum";
    }


    /**
     * 获取投注游戏状态 key
     *
     * @param tgGroupId tg 群id
     * @param tgBotId   tg 机器人id
     * @return key
     */
    public static String getBetStatusKey(String tgGroupId, String tgBotId) {
        return preFix + ":" + tgGroupId + ":" + tgBotId;
    }
}

