/*
 * tg学习代码
 */
package com.tggame.bet.vo;

import com.tggame.core.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 投注 分页 对象 VO
 *
 * @author tg
 */
@Data
public class BetOrderPageVO extends BaseVO implements java.io.Serializable {
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
     * 数据库字段:tg_group_id  属性显示:tg群id
     */
    @ApiModelProperty(value = "tg群id")
    private java.lang.String tgGroupId;
    /**
     * 数据库字段:user_id  属性显示:用户id
     */
    @ApiModelProperty(value = "用户id")
    private java.lang.String userId;
    /**
     * 数据库字段:tg_user_id  属性显示:tg下用户id
     */
    @ApiModelProperty(value = "tg下用户id")
    private java.lang.String tgUserId;
    /**
     * 数据库字段:bot_id  属性显示:机器人id
     */
    @ApiModelProperty(value = "机器人id")
    private java.lang.String botId;
    /**
     * 数据库字段:tg_bot_id  属性显示:机器人id
     */
    @ApiModelProperty(value = "tg下机器人id")
    private java.lang.String tgBotId;
    /**
     * 数据库字段:lottery_id  属性显示:彩种id
     */
    @ApiModelProperty(value = "彩种id")
    private java.lang.String lotteryId;
    /**
     * 数据库字段:issue  属性显示:期号
     */
    @ApiModelProperty(value = "期号")
    private java.lang.Long issue;
    /**
     * 数据库字段:lottery_name  属性显示:彩种
     */
    @ApiModelProperty(value = "彩种")
    private java.lang.String lotteryName;
    /**
     * 数据库字段:bet_id  属性显示:玩法id
     */
    @ApiModelProperty(value = "玩法id")
    private java.lang.String betId;
    /**
     * 数据库字段:bet_name  属性显示:玩法名
     */
    @ApiModelProperty(value = "玩法名")
    private java.lang.String betName;
    /**
     * 数据库字段:bet_code  属性显示:玩法编码
     */
    @ApiModelProperty(value = "玩法编码")
    private java.lang.String betCode;
    /**
     * 数据库字段:bet_num  属性显示:投注数字
     */
    @ApiModelProperty(value = "投注数字")
    private java.lang.String betNum;
    /**
     * 数据库字段:odds  属性显示:赔率
     */
    @ApiModelProperty(value = "赔率")
    private java.lang.Double odds;
    /**
     * 数据库字段:bet_type  属性显示:投注类型:两面 Both_Sides,号码 Num
     */
    @ApiModelProperty(value = "投注类型:两面 Both_Sides,号码 Num")
    private java.lang.String betType;
    /**
     * 数据库字段:pay_back_percent  属性显示:返奖率，如：97.5% 存储97.5
     */
    @ApiModelProperty(value = "返奖率，如：97.5% 存储97.5")
    private java.lang.Double payBackPercent;
    /**
     * 数据库字段:amount  属性显示:投注金额
     */
    @ApiModelProperty(value = "投注金额")
    private java.lang.Double amount;
    /**
     * 数据库字段:win_amount  属性显示:中奖金额
     */
    @ApiModelProperty(value = "中奖金额")
    private java.lang.Double winAmount;
    /**
     * 数据库字段:should_pay_amount  属性显示:应派奖金额=投注金额×赔率×返奖率
     */
    @ApiModelProperty(value = "应派奖金额=投注金额×赔率×返奖率")
    private java.lang.Double shouldPayAmount;
    /**
     * 数据库字段:status  属性显示:状态：投注 Bet，赢 Win，输 Lost，派彩中 Send_Money，已派彩  Sent_Money_Done，作废 Invalid
     */
    @ApiModelProperty(value = "状态：投注 Bet，赢 Win，输 Lost，派彩中 Send_Money，已派彩  Sent_Money_Done，作废 Invalid")
    private java.lang.String status;
    /**
     * 数据库字段:open_id  属性显示:开奖id
     */
    @ApiModelProperty(value = "开奖id")
    private java.lang.String openId;
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
