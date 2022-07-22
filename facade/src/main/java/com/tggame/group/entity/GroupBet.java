/*
 * tg学习代码
 */
package com.tggame.group.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 群玩法 的实体类
 *
 * @author tg
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GroupBet implements java.io.Serializable {
    /**
     * 数据库字段:id  属性显示:id
     */
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 数据库字段:group_id  属性显示:群id
     */
    @ApiModelProperty(value = "群id")
    private java.lang.String groupId;
    /**
     * 数据库字段:group_lottery_id  属性显示:群彩种id
     */
    @ApiModelProperty(value = "群彩种id")
    private java.lang.String groupLotteryId;
    /**
     * 数据库字段:bet_id  属性显示:玩法id
     */
    @ApiModelProperty(value = "玩法id")
    private java.lang.String betId;
    /**
     * 数据库字段:code  属性显示:编码
     */
    @ApiModelProperty(value = "编码")
    private java.lang.String code;
    /**
     * 数据库字段:min_bet_money  属性显示:最小投注额
     */
    @ApiModelProperty(value = "最小投注额")
    private java.lang.Double minBetMoney;
    /**
     * 数据库字段:max_bet_money  属性显示:最大投注额
     */
    @ApiModelProperty(value = "最大投注额")
    private java.lang.Double maxBetMoney;
    /**
     * 数据库字段:odds  属性显示:赔率
     */
    @ApiModelProperty(value = "赔率")
    private java.lang.Double odds;
    /**
     * 数据库字段:default_money  属性显示:默认投注额，tg键盘显示用
     */
    @ApiModelProperty(value = "默认投注额，tg键盘显示用")
    private java.lang.Double defaultMoney;
    /**
     * 数据库字段:type  属性显示:类型:两面 Both_Sides,号码 Num
     */
    @ApiModelProperty(value = "类型:两面 Both_Sides,号码 Num")
    private java.lang.String type;
    /**
     * 数据库字段:pay_back_percent  属性显示:返奖率,如：98.5% 存储 98.5
     */
    @ApiModelProperty(value = "返奖率,如：98.5% 存储 98.5")
    private java.lang.Double payBackPercent;
    /**
     * 数据库字段:status  属性显示:状态:启用 Enable，停用 Disable
     */
    @ApiModelProperty(value = "状态:启用 Enable，停用 Disable")
    private java.lang.String status;

}
