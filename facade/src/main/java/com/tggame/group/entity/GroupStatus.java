/*
* tg学习代码
 */
package com.tggame.group.entity;

/**
 * 状态:启用 Enable,停用 Disable 的实体类的状态
 *
 * @author tg
 */
public enum GroupStatus implements java.io.Serializable {
    Enable("启用"),
    Disable("停用"),
    ;

    public String val;

    GroupStatus(String val) {
        this.val = val;
    }

    /**
     * 根据状态名称查询状态
     *
     * @param stateName
     * @return
     */
    public static GroupStatus getEnum(String stateName) {
        for (GroupStatus enumName : GroupStatus.values()) {
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
