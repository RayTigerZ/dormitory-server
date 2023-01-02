package com.ray.dormitory.bean.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 性别枚举
 *
 * @author : Ray
 * @date : 2020.03.25 16:48
 */
@Getter
public enum Sex implements IEnum<String> {
    /**
     * 男生
     */
    MAN("男"),
    /**
     * 女生
     */
    WOMAN("女");

    @EnumValue
    @JsonValue
    private final String value;

    Sex(String value) {
        this.value = value;
    }

}
