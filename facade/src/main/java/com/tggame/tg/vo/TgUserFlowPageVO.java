/*
 * tg学习代码
 */
package com.tggame.tg.vo;

import io.swagger.annotations.ApiModelProperty;
import com.tggame.core.base.BaseVO;
import lombok.Data;

/**
 * 用户流水 分页 对象 VO
 *
 * @author tg
 */
@Data
public class TgUserFlowPageVO extends BaseVO implements java.io.Serializable {
    /**
     * 数据库字段:id  属性显示:id
     */
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 数据库字段:user_id  属性显示:用户id
     */
    @ApiModelProperty(value = "用户id")
    private java.lang.String userId;
    /**
     * 数据库字段:tg_username  属性显示:tg下用户名
     */
    @ApiModelProperty(value = "tg下用户名")
    private java.lang.String tgUsername;
    /**
     * 数据库字段:amount  属性显示:金额
     */
    @ApiModelProperty(value = "金额")
    private java.lang.Double amount;
    /**
     * 数据库字段:type  属性显示:类型: 充值 Recharge,投注支出 Bet_Paid,中奖收益 Bet_Win, 提现 Withdraw，投注退款 Bet_Refund
     */
    @ApiModelProperty(value = "类型: 充值 Recharge,投注支出 Bet_Paid,中奖收益 Bet_Win, 提现 Withdraw，投注退款 Bet_Refund")
    private java.lang.String type;
    /**
     * 数据库字段:status  属性显示:状态: 审核中 Audit,已审核 Done
     */
    @ApiModelProperty(value = "状态: 审核中 Audit,已审核 Done")
    private java.lang.String status;
    /**
     * 数据库字段:from  属性显示:usdt发起者地址
     */
    @ApiModelProperty(value = "usdt发起者地址")
    private java.lang.String from;
    /**
     * 数据库字段:to  属性显示:usdt收款地址
     */
    @ApiModelProperty(value = "usdt收款地址")
    private java.lang.String to;
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
    /**
     * 数据库字段:summary  属性显示:说明
     */
    @ApiModelProperty(value = "说明")
    private java.lang.String summary;

}
