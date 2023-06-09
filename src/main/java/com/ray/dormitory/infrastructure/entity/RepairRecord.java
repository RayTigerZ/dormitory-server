package com.ray.dormitory.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>
 * 维修记录
 * </p>
 *
 * @author Ray
 * @date 2020-2-24 16:41:23
 */
@Getter
@Setter
@TableName("repair_record")
public class RepairRecord implements Serializable {


    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 宿舍号
     */
    @NotNull
    private String roomNum;

    /**
     * 维修问题
     */
    @NotBlank
    @Size(min = 1, max = 128)
    private String problem;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 维修人员
     */
    private String repairer;

    /**
     * 预约上门时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime appointTime;

    /**
     * 维修反馈
     */
    private String feedback;

    /**
     * 报修学生
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;


}
