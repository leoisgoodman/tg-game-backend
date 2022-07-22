/*
 * tg学习代码
 */
package com.tggame.lottery.vo;

import com.tggame.core.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 彩种 分页 对象 VO
 *
 * @author tg
 */
@Data
public class LotteryPageVO extends BaseVO implements java.io.Serializable {
    /**
     * 数据库字段:id  属性显示:id
     */
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 数据库字段:name  属性显示: 名称
     */
    @ApiModelProperty(value = " 名称")
    private java.lang.String name;
    /**
     * 数据库字段:pre_id  属性显示:上级id
     */
    @ApiModelProperty(value = "上级id")
    private java.lang.String preId;
    /**
     * 数据库字段:code  属性显示:编码
     */
    @ApiModelProperty(value = "编码")
    private java.lang.String code;
    /**
     * 数据库字段:status  属性显示:状态:启用 Enable,停用 Disable
     */
    @ApiModelProperty(value = "状态:启用 Enable,停用 Disable")
    private java.lang.String status;
    /**
     * 数据库字段:summary  属性显示:说明
     */
    @ApiModelProperty(value = "说明")
    private java.lang.String summary;

}
