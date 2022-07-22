/*
 * tg学习代码
 */
package com.tggame.user.vo;

import io.swagger.annotations.ApiModelProperty;
import com.tggame.core.base.BaseVO;
import lombok.Data;

/**
 * 群成员 分页 对象 VO
 *
 * @author tg
 */
@Data
public class UserPageVO extends BaseVO implements java.io.Serializable {
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
     * 数据库字段:tg_user_id  属性显示:tg用户id，来自tg
     */
    @ApiModelProperty(value = "tg用户id，来自tg")
    private java.lang.String tgUserId;
    /**
     * 数据库字段:tg_username  属性显示:tg下用户名
     */
    @ApiModelProperty(value = "tg下用户名")
    private java.lang.String tgUsername;
    /**
     * 数据库字段:username  属性显示:本系统生成的用户名，默认和tg保持一致
     */
    @ApiModelProperty(value = "本系统生成的用户名，默认和tg保持一致")
    private java.lang.String username;
    /**
     * 数据库字段:password  属性显示:密码，默认和用户名一致
     */
    @ApiModelProperty(value = "密码，默认和用户名一致")
    private java.lang.String password;
    /**
     * 数据库字段:google_code  属性显示:谷歌口令
     */
    @ApiModelProperty(value = "谷歌口令")
    private java.lang.String googleCode;
    /**
     * 数据库字段:percent  属性显示:占成比例，如:10% 存储为10
     */
    @ApiModelProperty(value = "占成比例，如:10% 存储为10")
    private java.lang.Integer percent;
    /**
     * 数据库字段:status  属性显示:状态:可投注 Enable,禁止投注 Disable，庄审核 Verify_Banker
     */
    @ApiModelProperty(value = "状态:可投注 Enable,禁止投注 Disable，庄审核 Verify_Banker")
    private java.lang.String status;
    /**
     * 数据库字段:type  属性显示:类型:试玩 Demo,玩家 Player,庄家 Banker
     */
    @ApiModelProperty(value = "类型:试玩 Demo,玩家 Player,庄家 Banker")
    private java.lang.String type;
    /**
     * 数据库字段:is_owner  属性显示:是否群主：是 Y,否 N
     */
    @ApiModelProperty(value = "是否群主：是 Y,否 N")
    private java.lang.String isOwner;
    /**
     * 数据库字段:is_admin  属性显示:是否管理员：是 Y，否 N
     */
    @ApiModelProperty(value = "是否管理员：是 Y，否 N")
    private java.lang.String isAdmin;
    /**
     * 数据库字段:usdt_balance  属性显示:usdt 余额
     */
    @ApiModelProperty(value = "usdt 余额")
    private java.lang.Double usdtBalance;
    /**
     * 数据库字段:usdt_address  属性显示:usdt 地址
     */
    @ApiModelProperty(value = "usdt 地址")
    private java.lang.String usdtAddress;
    /**
     * 数据库字段:bet_total_money  属性显示:投注总额
     */
    @ApiModelProperty(value = "投注总额")
    private java.lang.Long betTotalMoney;
    /**
     * 数据库字段:bet_win_money  属性显示:投注盈利
     */
    @ApiModelProperty(value = "投注盈利")
    private java.lang.Long betWinMoney;
    /**
     * 数据库字段:bet_lost_money  属性显示:投注亏损总额
     */
    @ApiModelProperty(value = "投注亏损总额")
    private java.lang.Long betLostMoney;
    /**
     * 数据库字段:summary  属性显示:说明
     */
    @ApiModelProperty(value = "说明")
    private java.lang.String summary;
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
