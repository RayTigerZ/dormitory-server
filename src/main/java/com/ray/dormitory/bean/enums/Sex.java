package com.ray.dormitory.bean.enums;

/**
 * 性别枚举
 *
 * @author : Ray
 * @date : 2020.03.25 16:48
 */
public enum Sex {
    MAN("男"), WOMAN("女");

    private String value;

    private Sex(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
