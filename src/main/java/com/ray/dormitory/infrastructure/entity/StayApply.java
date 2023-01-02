package com.ray.dormitory.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ray.dormitory.export.Export;
import com.ray.dormitory.web.validator.group.UpdateValid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class StayApply implements Serializable, Export {

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

    @JsonIgnore
    private static List<String> key;
    @JsonIgnore
    private static List<String> header;

    static {
        key = new ArrayList<>();
        key.add("studentName");
        key.add("studentNum");
        key.add("beginDate");
        key.add("endDate");
        key.add("emergencyContact");
        key.add("reason");
        key.add("remark");
        key.add("createTime");
        key.add("isConsent");
        key.add("processTime");

        header = new ArrayList<>();
        header.add("学生姓名");
        header.add("学号");
        header.add("开始时间");
        header.add("结束时间");
        header.add("紧急联系方式");
        header.add("原因");
        header.add("备注");
        header.add("申请时间");
        header.add("审核结果");
        header.add("审核时间");

    }

    @Override
    public List<String> getKey() {
        return key;
    }

    @Override
    public List<String> getHeader() {
        return header;
    }

}
