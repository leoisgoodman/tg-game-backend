/*
* tg学习代码
 */
package com.tggame.open.entity;

/**
 * 状态：就绪 Ready，可投注 Enable，封盘  Lock，已开奖 Drawn ,作废  Invalid 的实体类的状态
 *
 * @author tg
 */
public enum OpenRecordStatus implements java.io.Serializable {
    Ready("就绪"),
    Enable("可投注"),
    Drawn("已开奖"),
    ;

    public String val;

    OpenRecordStatus(String val) {
        this.val = val;
    }

    /**
     * 根据状态名称查询状态
     *
     * @param stateName
     * @return
     */
    public static OpenRecordStatus getEnum(String stateName) {
        for (OpenRecordStatus enumName : OpenRecordStatus.values()) {
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
