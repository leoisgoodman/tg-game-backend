/*
 * tg学习代码
 */
package com.tggame.tg.vo;

import io.swagger.annotations.ApiModelProperty;
import com.tggame.core.base.BaseVO;
import lombok.Data;

/**
 * 群机器人 分页 对象 VO
 *
 * @author tg
 */
@Data
public class TgGroupBotPageVO extends BaseVO implements java.io.Serializable {
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
     * 数据库字段:bot_id  属性显示:机器人id
     */
    @ApiModelProperty(value = "机器人id")
    private java.lang.String botId;

}
