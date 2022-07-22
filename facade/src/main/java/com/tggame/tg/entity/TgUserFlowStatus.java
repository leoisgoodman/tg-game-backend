/*
 * tg学习代码
 */
package com.tggame.tg.entity;

/**
 * 状态: 审核中 Audit,已审核 Done 的实体类的状态
 *
 * @author tg
 */
public enum TgUserFlowStatus implements java.io.Serializable {
    Audit("审核中"),
    Done("已审核"),
    ;

    public String val;

    TgUserFlowStatus(String val) {
        this.val = val;
    }

    /**
     * 根据状态名称查询状态
     *
     * @param stateName
     * @return
     */
    public static TgUserFlowStatus getEnum(String stateName) {
        for (TgUserFlowStatus enumName : TgUserFlowStatus.values()) {
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
