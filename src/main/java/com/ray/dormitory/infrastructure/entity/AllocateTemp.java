package com.ray.dormitory.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.ray.dormitory.bean.enums.Sex;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 宿舍预分配结果表
 * </p>
 *
 * @author Ray
 * @date 2020-3-23 21:10:14
 */
@Getter
@Setter
@TableName("allocate_temp")
public class AllocateTemp implements Serializable {


    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 学号
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String studentNum;

    /**
     * 学生姓名
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String name;

    /**
     * 性别
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Sex sex;

    /**
     * 宿舍号
     */
    private String roomNum;

    /**
     * 问卷调查ID
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Integer surveyId;


}
