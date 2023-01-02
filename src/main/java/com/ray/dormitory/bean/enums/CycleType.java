package com.ray.dormitory.bean.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 统计周期类型
 *
 * @author : Ray
 * @date : 2020.04.25 18:28
 */
@Getter
public enum CycleType implements IEnum<Integer> {
    /**
     * 按年统计，如：2020,2019,2018...
     */
    Year(1, "%Y"),
    /**
     * 按月统计，如：2020-02,2020-01，2019-12,2019-11...
     */
    MONTH(2, "%Y-%m");

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String format;

    CycleType(Integer value, String format) {
        this.value = value;

        this.format = format;
    }
}
