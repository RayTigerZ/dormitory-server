package com.ray.dormitory.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ray.dormitory.bean.bo.Question;
import com.ray.dormitory.util.CustomHandler;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 性格特征问卷
 * <p>
 *
 * @author : Ray
 * @date : 2019.12.07 16:01
 */
@Getter
@Setter
@TableName(value = "questionnaire", autoResultMap = true)
@JsonIgnoreProperties(value = {"handler"})
public class Questionnaire implements Serializable {
    /**
     * 问卷ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 问卷标题
     */
    @NotBlank(message = "问卷标题不能为空")
    @Size(min = 1, max = 32, message = "问卷标题长度不超过32")
    private String title;

    /**
     * 问卷发布状态
     * true：已发布
     * false：未发布
     */
    @NotNull(message = "问卷发布状态不能为空")
    private Boolean published;

    /**
     * 问卷内容
     */
    @TableField(typeHandler = CustomHandler.class)
    private List<Question> questions;

}
