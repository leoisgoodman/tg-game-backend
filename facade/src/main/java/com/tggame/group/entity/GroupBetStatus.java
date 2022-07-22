/*
* tg学习代码
 */
package com.tggame.group.entity;

/**
 * 状态:启用 Enable，停用 Disable 的实体类的状态
 *
 * @author tg
 */
public enum GroupBetStatus implements java.io.Serializable {
    Enable("启用"),
    Disable("停用"),
    ;

    public String val;

    GroupBetStatus(String val) {
        this.val = val;
    }

    /**
     * 根据状态名称查询状态
     *
     * @param stateName
     * @return
     */
    public static GroupBetStatus getEnum(String stateName) {
        for (GroupBetStatus enumName : GroupBetStatus.values()) {
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
