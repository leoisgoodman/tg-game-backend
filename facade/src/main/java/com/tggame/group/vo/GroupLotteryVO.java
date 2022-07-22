/**
 * tg学习代码
 */
package com.tggame.group.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 群彩种 VO
 *
 * @author tg
 */
@Data
public class GroupLotteryVO implements java.io.Serializable {

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
     * 数据库字段:lottery_id  属性显示:彩种id
     */
    @ApiModelProperty(value = "彩种id")
    private java.lang.String lotteryId;

}
