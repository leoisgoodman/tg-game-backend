/*
 * tg学习代码
 */
package com.tggame.bet.entity;

/**
 * @author tg
 */
public enum BetCode implements java.io.Serializable {
    Big("big"),
    Small("small"),
    Odd("odd"),
    Even("even"),
    Num("num"),
    ;

    public String val;

    BetCode(String val) {
        this.val = val;
    }

    /**
     * 根据状态名称查询状态
     *
     * @param stateName
     * @return
     */
    public static BetCode getEnum(String stateName) {
        for (BetCode enumName : BetCode.values()) {
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
