/*
 * tg学习代码
 */
package com.tggame.open.vo;

import com.tggame.core.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 开奖记录 分页 对象 VO
 *
 * @author tg
 */
@Data
public class OpenRecordPageVO extends BaseVO implements java.io.Serializable {
    /**
     * 数据库字段:id  属性显示:id
     */
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 数据库字段:lottery_id  属性显示:彩种id
     */
    @ApiModelProperty(value = "彩种id")
    private java.lang.String lotteryId;
    /**
     * 数据库字段:issue  属性显示:期号
     */
    @ApiModelProperty(value = "期号")
    private java.lang.Long issue;
    /**
     * 数据库字段:num  属性显示:开奖号码
     */
    @ApiModelProperty(value = "开奖号码")
    private java.lang.String num;
    /**
     * 数据库字段:status  属性显示:状态：就绪 Ready，可投注 Enable，封盘  Lock，已开奖 Drawn ,作废  Invalid
     */
    @ApiModelProperty(value = "状态：就绪 Ready，可投注 Enable，封盘  Lock，已开奖 Drawn ,作废  Invalid")
    private java.lang.String status;
    /**
     * 数据库字段:create_time  属性显示:创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 数据库字段:update_time  属性显示:更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;

}
