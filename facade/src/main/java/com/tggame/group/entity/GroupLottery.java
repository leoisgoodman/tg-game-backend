/*
 * tg学习代码
 */
package com.tggame.group.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 群彩种 的实体类
 *
 * @author tg
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GroupLottery implements java.io.Serializable {
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
