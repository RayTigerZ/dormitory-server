package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

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
@TableName("answer")
public class Answer implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 学生ID
     */
    private Integer userId;

    /**
     * 问卷调查ID
     */
    private Integer surveyId;

    /**
     * 问卷调查答案
     */
    private Double[] answer;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String createUser;

    @Override
    public boolean equals(Object a) {
        if (a instanceof Answer) {
            Double[] answer = ((Answer) a).getAnswer();
            int len = this.answer.length;
            for (int i = 0; i < len; i++) {
                if (!this.answer[i].equals(answer[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return Arrays.toString(answer);
    }
}
