package com.ray.dormitory.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author : Ray
 * @date : 2019.12.05 15:27
 */
@Getter
@Setter
@TableName(value = "operation", resultMap = "operationMap")
@JsonIgnoreProperties(value = {"handler"})
public class Operation implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 操作名称
     */
    @NotBlank(message = "操作名称不能为空")
    @Size(min = 1, max = 32, message = "操作名称长度不超过32")
    private String opName;

    /**
     * 接口uri
     */
    @NotBlank(message = "uri不能为空")
    @Size(min = 1, max = 64, message = "uri长度不超过32")
    private String uri;

    /**
     * 请求方法（GET，POST，PUT，DELETE）
     */
    @NotBlank(message = "请求方法不能为空")
    private String method;

    /**
     * 菜单ID
     */
    @NotNull(message = "菜单ID不能为空")
    private Integer menuId;

    @TableField(exist = false)
    private List<String> roles;
}
