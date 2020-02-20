package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 来访记录表
 * </p>
 *
 * @author Ray
 * @date 2020-2-14 17:23:00
 */
@Getter
@Setter
@TableName("visit_record")
public class VisitRecord implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 访客真实姓名
     */
    @NotNull(message = "真实姓名不能为空")
    @Size(min = 1, max = 32, message = "真实姓名长度在1到32")
    private String trueName;

    /**
     * 访客身份证
     */
    @NotNull(message = "身份证不能为空")
    @Size(min = 1, max = 32, message = "身份证长度在1到32")
    private String identity;

    /**
     * 访客来访时间
     */
    @NotNull(message = "来访时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date visitTime;

    /**
     * 访客离开时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date leaveTime;

    /**
     * 备注特别情况
     */
    @Size(max = 128, message = "备注信息最多为128个字符")
    private String remark;


}
