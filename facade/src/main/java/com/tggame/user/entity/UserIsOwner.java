/*
 * tg学习代码
 */
package com.tggame.user.entity;

/**
 * 是否群主：是 Y,否 N 的实体类的状态
 *
 * @author tg
 */
public enum UserIsOwner implements java.io.Serializable {
    Y("是"),
    N("否"),
    ;

    public String val;

    UserIsOwner(String val) {
        this.val = val;
    }

    /**
     * 根据状态名称查询状态
     *
     * @param stateName
     * @return
     */
    public static UserIsOwner getEnum(String stateName) {
        for (UserIsOwner enumName : UserIsOwner.values()) {
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
