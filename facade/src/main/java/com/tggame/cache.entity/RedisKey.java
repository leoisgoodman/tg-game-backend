package com.tggame;


public enum RedisKey {
    ,
    ;

    private static final String preFix = "tggame";

    public static String getCachekey() {
        return preFix + "enum";
    }


}

