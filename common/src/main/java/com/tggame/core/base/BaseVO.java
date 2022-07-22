package com.tggame.core.base;


import lombok.Data;

import java.util.Date;

@Data
public class BaseVO implements java.io.Serializable {

    //日期格式化
    protected static final String DATE_FORMAT = "yyyy-MM-dd";

//    protected Long id;
    //时间格式化
    protected static final String TIME_FORMAT = "HH:mm:ss";
    //日期时间格式化
    protected static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    //时间戳格式化
    protected static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
    private static final long serialVersionUID = -7200095849148417467L;
    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    protected Date createTimeBegin;
    protected Date createTimeEnd;
    /**
     * 更新时间 yyy-MM-dd HH:mm:ss
     */
    protected Date updateTimeBegin;
    protected Date updateTimeEnd;


}
