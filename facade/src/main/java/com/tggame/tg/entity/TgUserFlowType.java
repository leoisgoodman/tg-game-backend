/*
* tg学习代码
 */
package com.tggame.tg.entity;

/**
 * 类型: 充值 Recharge,投注支出 Bet_Paid,中奖收益 Bet_Win, 提现 Withdraw，投注退款 Bet_Refund 的实体类的状态
 *
 * @author tg
 */
public enum TgUserFlowType implements java.io.Serializable {
    Bet_Refund("投注退款"),
    Withdraw("提现"),
    Recharge("充值"),
    Bet_Win("中奖收益"),
    Bet_Paid("投注支出"),
    ;

    public String val;

    TgUserFlowType(String val) {
        this.val = val;
    }

    /**
     * 根据状态名称查询状态
     *
     * @param stateName
     * @return
     */
    public static TgUserFlowType getEnum(String stateName) {
        for (TgUserFlowType enumName : TgUserFlowType.values()) {
            if (enumName.name().equalsIgnoreCase(stateName)) {
                return enumName;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }

}
