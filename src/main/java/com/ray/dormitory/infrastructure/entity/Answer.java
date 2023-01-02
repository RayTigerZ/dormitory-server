package com.ray.dormitory.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 学生问卷调查答案
 * </p>
 *
 * @author Ray
 * @date 2020-1-13 16:04:45
 */
@Getter
@Setter
@TableName(value = "answer", autoResultMap = true)
public class Answer implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 学生ID
     */
    @NotNull(message = "学生不能为空")
    private Integer userId;

    /**
     * 问卷调查ID
     */
    @NotNull(message = "问卷调查不能为空")
    private Integer surveyId;

    /**
     * 问卷调查答案
     */
    @NotEmpty(message = "答案不能为空")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Double> answer;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;


    @Override
    public boolean equals(Object a) {
        if (a instanceof Answer) {
            List<Double> answer = ((Answer) a).getAnswer();
            int len = this.answer.size();
            for (int i = 0; i < len; i++) {
                if (!this.answer.get(i).equals(answer.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return answer.toString();
    }
}
