/*
 * tg学习代码
 */
package com.tggame.bot.vo;

import io.swagger.annotations.ApiModelProperty;
import com.tggame.core.base.BaseVO;
import lombok.Data;

/**
 * tg机器人 分页 对象 VO
 *
 * @author tg
 */
@Data
public class BotPageVO extends BaseVO implements java.io.Serializable {
    /**
     * 数据库字段:id  属性显示:id
     */
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 数据库字段:tg_bot_id  属性显示:tg 机器人id
     */
    @ApiModelProperty(value = "tg 机器人id")
    private java.lang.String tgBotId;
    /**
     * 数据库字段:name  属性显示:tg机器人名称
     */
    @ApiModelProperty(value = "tg机器人名称")
    private java.lang.String name;
    /**
     * 数据库字段:tg_token  属性显示:tg授权机器人token,tg中获得
     */
    @ApiModelProperty(value = "tg授权机器人token,tg中获得")
    private java.lang.String tgToken;
    /**
     * 数据库字段:bot_owner_username  属性显示:机器人创建的tg账号
     */
    @ApiModelProperty(value = "机器人创建的tg账号")
    private java.lang.String botOwnerUsername;
    /**
     * 数据库字段:tg_phone  属性显示:手机号
     */
    @ApiModelProperty(value = "手机号")
    private java.lang.String tgPhone;
    /**
     * 数据库字段:share_link  属性显示:机器人链接
     */
    @ApiModelProperty(value = "机器人链接")
    private java.lang.String shareLink;
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
     * 数据库字段:update_time  属性显示:更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;

}
