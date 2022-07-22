/*
* tg学习代码
 */
package com.tggame.user.entity;

/**
 * 类型:试玩 Demo,玩家 Player,庄家 Banker 的实体类的状态
 *
 * @author tg
 */
public enum UserType implements java.io.Serializable {
    Player("玩家"),
    Demo("试玩"),
    Banker("庄家"),
    ;

    public String val;

    UserType(String val) {
        this.val = val;
    }

    /**
     * 根据状态名称查询状态
     *
     * @param stateName
     * @return
     */
    public static UserType getEnum(String stateName) {
        for (UserType enumName : UserType.values()) {
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
