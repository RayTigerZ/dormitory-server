package com.ray.dormitory.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 宿舍费用项
 * </p>
 *
 * @author Ray
 * @date 2020-2-26 13:07:34
 */
@Getter
@Setter
@TableName("charge")
public class Charge implements Serializable {


    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 费用项名称
     * note: 默认值，不更新
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String name;

    /**
     * 每单位费用
     */
    private Double price;

    /**
     * 单位
     * note: 默认值，不更新
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String unit;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String modifyUser;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;


}
