/*
* tg学习代码
 */
package com.tggame.group.entity;

/**
 * 类型:两面 Both_Sides,号码 Num 的实体类的状态
 *
 * @author tg
 */
public enum GroupBetType implements java.io.Serializable {
    Both_Sides("两面"),
    Num("号码"),
    ;

    public String val;

    GroupBetType(String val) {
        this.val = val;
    }

    /**
     * 根据状态名称查询状态
     *
     * @param stateName
     * @return
     */
    public static GroupBetType getEnum(String stateName) {
        for (GroupBetType enumName : GroupBetType.values()) {
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
