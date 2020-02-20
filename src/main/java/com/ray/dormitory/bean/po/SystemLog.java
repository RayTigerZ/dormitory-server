package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author Ray Z
 * @date 2019/11/23 16:21
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName("system_log")
public class SystemLog {


    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 操作功能
     */
    private String optype;
    /**
     * 操作IP地址
     */

    private String remoteAddr;
    /**
     * 请求URI
     */
    private String requestUri;
    /**
     * 操作方式
     */
    private String httpMethod;
    /**
     * 操作提交的数据
     */
    private String params;
    /**
     * 返回的数据
     */
    private String outBody;
    /**
     * 方法执行时间
     */
    private Long useTime;
    /**
     * 浏览器信息
     */
    private String browser;
    /**
     * 操作者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String remark;

}