/*
 * tg学习代码
 */
package com.tggame.bet.entity;

/**
 * 状态：投注 Bet，赢 Win，输 Lost，派彩中 Send_Money，已派彩  Sent_Money_Done，作废 Invalid 的实体类的状态
 *
 * @author tg
 */
public enum BetOrderStatus implements java.io.Serializable {
    Bet("投注"),
    Lost("输"),
    Send_Money("派彩中"),
    Win("赢"),
    Invalid("作废"),
    Sent_Money_Done("已派彩"),
    ;

    public String val;

    BetOrderStatus(String val) {
        this.val = val;
    }

    /**
     * 根据状态名称查询状态
     *
     * @param stateName
     * @return
     */
    public static BetOrderStatus getEnum(String stateName) {
        for (BetOrderStatus enumName : BetOrderStatus.values()) {
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
