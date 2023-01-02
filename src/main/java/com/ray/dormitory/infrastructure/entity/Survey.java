package com.ray.dormitory.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ray.dormitory.bean.enums.AllocateStatus;
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
 * 问卷调查
 * </p>
 *
 * @author Ray
 * @date 2020-1-12 2:12:36
 */
@Getter
@Setter
@TableName(value = "survey", resultMap = "surveyResultMap")
@JsonIgnoreProperties({"handler"})
public class Survey implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 问卷调查名称
     */
    @NotBlank(message = "问卷调查名称不能为空")
    @Size(max = 32, message = "问卷调查名称长度不能超过32")
    private String name;

    /**
     * 问卷ID
     */
    private Integer questionnaireId;

    /**
     * 问卷
     */
    @TableField(exist = false)
    private String questionnaireName;

    /**
     * 问卷调查针对的年级
     */
    @NotNull(message = "问卷调查针对的年级不能为空")
    private Integer grade;

    /**
     * 填写问卷调查的开始时间
     */
    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    /**
     * 填写问卷调查的结束时间
     */
    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    /**
     * 是否完成分配
     */
    private AllocateStatus status;

    /**
     * 问卷调查的创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUser;


    /**
     * 问卷调查的创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;


}
