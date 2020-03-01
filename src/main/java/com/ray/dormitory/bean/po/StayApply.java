package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ray.dormitory.group.UpdateValid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 留校申请
 * </p>
 *
 * @author Ray
 * @date 2020-2-18 19:24:32
 */
@Getter
@Setter
@TableName("stay_apply")
public class StayApply implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 学号
     */
    @NotBlank(message = "学号不能为空", groups = {UpdateValid.class})
    private String studentNum;

    /**
     * 学生姓名
     */
    @NotBlank(message = "学生姓名不能为空", groups = {UpdateValid.class})
    private String studentName;

    /**
     * 留宿开始时间
     */
    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date beginDate;

    /**
     * 留宿结束时间
     */
    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    /**
     * 紧急联系电话
     */
    @Pattern(regexp = "^1[3456789][0-9]{9}$", message = "请输入正确的手机号码")
    private String emergencyContact;

    /**
     * 留校原因
     */
    @NotBlank(message = "留校原因不能为空")
    @Size(max = 128, message = "留校原因长度不能超过128")
    private String reason;

    /**
     * 备注
     */
    @Size(max = 128, message = "备注长度不能超过128")
    private String remark;

    /**
     * 申请时间
     */
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否同意
     */
    private Boolean isConsent;

    /**
     * 处理时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date processTime;


}
