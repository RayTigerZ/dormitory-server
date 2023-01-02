package com.ray.dormitory.infrastructure.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ray.dormitory.bean.enums.Sex;
import com.ray.dormitory.web.validator.group.SaveByFileValid;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author Ray
 * @date 2019/11/23 16:21
 */
@Getter
@Setter
@TableName(value = "user", resultMap = "userResultMap")
@JsonIgnoreProperties(value = {"handler"})
public class User implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户真实姓名
     */
    @ExcelProperty("姓名")
    @NotBlank(message = "姓名不能为空")
    @Size(min = 1, max = 16, message = "姓名长度在1到32")
    private String name;

    /**
     * 登录帐号(学号)
     */
    @ExcelProperty("帐号")
    @NotBlank(message = "帐号不能为空")
    @Size(min = 8, max = 12, message = "帐号长度在8到12")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String account;

    /**
     * 密码(加密)
     */
    @JsonIgnore
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String password;

    /**
     * 加密盐值
     */
    @JsonIgnore
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String salt;

    /**
     * 手机号码
     */
    @ExcelProperty("手机号码")
    @Size(max = 11, message = "手机号码长度小于等于11")
    private String phone;

    /**
     * 电子邮箱
     */
    @ExcelProperty("邮箱")
    @Email(message = "邮箱格式错误")
    private String email;

    /**
     * 性别
     */
    @ExcelProperty("性别")
    private Sex sex;

    /**
     * 房间号
     */
    private String roomNum;

    /**
     * 帐号可用性
     */
    @NotNull
    private Boolean isUsable;

    /**
     * 班级ID
     */
    private Integer classId;

    /**
     * 班级名称
     */
    @ExcelProperty("班级")
    @TableField(exist = false)
    @NotBlank(message = "班级名称不能为空", groups = {SaveByFileValid.class})
    private String cla;

    /**
     * 专业
     */
    @TableField(exist = false)
    private String major;

    /**
     * 学院名称
     */
    @TableField(exist = false)
    private String college;

    @TableField(exist = false)
    private String roles;

    @TableField(exist = false)
    private Set<Integer> roleIds;


}
