package com.ray.dormitory.bean.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 菜单类型
 *
 * @author : Ray
 * @date : 2020.03.29 13:45
 */
@Getter
public enum MenuType implements IEnum<Integer> {
    CATALOG(1, "目录"),
    MENU(2, "菜单");

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String desc;

    @JsonCreator
    MenuType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;

    }
}
