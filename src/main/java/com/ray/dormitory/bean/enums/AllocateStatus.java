package com.ray.dormitory.bean.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * survey分配状态
 *
 * @author : Ray
 * @date : 2020.03.29 2:05
 */
@Getter
public enum AllocateStatus implements IEnum<Integer> {
    NOT_ALLOCATE(0, "未分配"),
    PRE_ALLOCATE(1, "预分配"),
    ALLOCATED(2, "已分配");

    @EnumValue
    private final Integer value;
    @JsonValue
    private final String desc;

    AllocateStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
