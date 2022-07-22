/*
 * tg学习代码
 */
package com.tggame.group.vo;

import io.swagger.annotations.ApiModelProperty;
import com.tggame.core.base.BaseVO;
import lombok.Data;

/**
 * tg群 分页 对象 VO
 *
 * @author tg
 */
@Data
public class GroupPageVO extends BaseVO implements java.io.Serializable {
    /**
     * 数据库字段:id  属性显示:id
     */
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 数据库字段:merchant_id  属性显示:
     */
    @ApiModelProperty(value = "")
    private java.lang.String merchantId;
    /**
     * 数据库字段:name  属性显示:群名称
     */
    @ApiModelProperty(value = "群名称")
    private java.lang.String name;
    /**
     * 数据库字段:tg_group_id  属性显示:tg群id
     */
    @ApiModelProperty(value = "tg群id")
    private java.lang.String tgGroupId;
    /**
     * 数据库字段:tg_game_code  属性显示:tg小游戏编码
     */
    @ApiModelProperty(value = "tg小游戏编码")
    private java.lang.String tgGameCode;
    /**
     * 数据库字段:domain  属性显示:群绑定的小游戏域名
     */
    @ApiModelProperty(value = "群绑定的小游戏域名")
    private java.lang.String domain;
    /**
     * 数据库字段:status  属性显示:状态:启用 Enable,停用 Disable
     */
    @ApiModelProperty(value = "状态:启用 Enable,停用 Disable")
    private java.lang.String status;
    /**
     * 数据库字段:create_time  属性显示:创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 数据库字段:summary  属性显示:群说明
     */
    @ApiModelProperty(value = "群说明")
    private java.lang.String summary;
    /**
     * 数据库字段:game_summary  属性显示:群游戏欢迎说明
     */
    @ApiModelProperty(value = "群游戏欢迎说明")
    private java.lang.String gameSummary;

}
