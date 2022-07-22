/**
 * tg学习代码
 */
package com.tggame.group.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 退群 VO
 *
 * @author tg
 */
@Data
public class LeftGroupVO implements java.io.Serializable {

    @ApiModelProperty(value = "tg群id")
    private String tgGroupId;

    @ApiModelProperty(value = "tg机器人id")
    private String tgBotId;

    @ApiModelProperty(value = "tg 用户id")
    private String tgUserId;


}
