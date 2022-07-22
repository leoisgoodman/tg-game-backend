/*
 * tg学习代码
 */
package com.tggame.bet.entity;

/**
 * 状态:启用 Enable，停用 Disable 的实体类的状态
 *
 * @author tg
 */
public enum BetStatus implements java.io.Serializable {
    Enable("启用"),
    Disable("停用"),
    ;

    public String val;

    BetStatus(String val) {
        this.val = val;
    }

    /**
     * 根据状态名称查询状态
     *
     * @param stateName
     * @return
     */
    public static BetStatus getEnum(String stateName) {
        for (BetStatus enumName : BetStatus.values()) {
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
