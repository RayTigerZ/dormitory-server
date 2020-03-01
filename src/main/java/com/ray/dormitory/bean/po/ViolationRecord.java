package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 违规记录表
 * </p>
 *
 * @author Ray
 * @date 2020-2-15 16:37:24
 */
@Getter
@Setter
@TableName("violation_record")
public class ViolationRecord implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 学生学号
     */
    @NotBlank(message = "学生学号不能为空")
    private String studentNum;

    /**
     * 学生姓名
     */
    @NotBlank(message = "学生姓名不能为空")
    private String studentName;

    /**
     * 违规事项
     */
    @NotBlank(message = "违规事项不能为空")
    @Size(min = 1, max = 64, message = "违规事项长度在1到64")
    private String violation;

    /**
     * 处罚
     */
    @Size(max = 64, message = "处罚最多为64个字符")
    private String punishment;

    /**
     * 备注
     */
    @Size(max = 128, message = "备注最多为128个字符")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUser;


}
