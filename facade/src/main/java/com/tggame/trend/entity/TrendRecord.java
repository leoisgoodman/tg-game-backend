/*
 * tg学习代码
 */
package com.tggame.trend.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 走势记录 的实体类
 *
 * @author tg
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TrendRecord implements java.io.Serializable {
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
    private java.lang.Integer issue;
    /**
     * 数据库字段:data  属性显示:走势数据
     */
    @ApiModelProperty(value = "走势数据")
    private java.lang.String data;
    /**
     * 数据库字段:open_time  属性显示:开奖时间
     */
    @ApiModelProperty(value = "开奖时间")
    private java.util.Date openTime;
    /**
     * 数据库字段:create_time  属性显示:创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

}
