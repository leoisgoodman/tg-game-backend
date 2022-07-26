package com.tggame.cache.entity;

/**
 * 常量枚举定义，用于表达常量内容的定义
 */
public enum ConstantsEnum {
    Jwt_Secret("c5f04615-7739-4267-877c-123d9ab7fed1"),
    ;
    public String value;

    ConstantsEnum(String value) {
        this.value = value;
    }

}
