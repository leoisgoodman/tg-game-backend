/*
 * tg学习代码
 */
package com.tggame.user.entity;

/**
 * 状态:可投注 Enable,禁止投注 Disable，庄审核 Verify_Banker 的实体类的状态
 *
 * @author tg
 */
public enum UserStatus implements java.io.Serializable {
    Verify_Banker("庄审核"),
    Enable("可投注"),
    Disable("禁止投注"),
    ;

    public String val;

    UserStatus(String val) {
        this.val = val;
    }

    /**
     * 根据状态名称查询状态
     *
     * @param stateName
     * @return
     */
    public static UserStatus getEnum(String stateName) {
        for (UserStatus enumName : UserStatus.values()) {
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
