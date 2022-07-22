/*
* tg学习代码
 */
package com.tggame.user.entity;

/**
 * 是否管理员：是 Y，否 N 的实体类的状态
 *
 * @author tg
 */
public enum UserIsAdmin implements java.io.Serializable {
    Y("是"),
    N("否"),
    ;

    public String val;

    UserIsAdmin(String val) {
        this.val = val;
    }

    /**
     * 根据状态名称查询状态
     *
     * @param stateName
     * @return
     */
    public static UserIsAdmin getEnum(String stateName) {
        for (UserIsAdmin enumName : UserIsAdmin.values()) {
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
