package com.ray.dormitory.bean.po;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.28 18:54
 */
@Getter
@Setter
@TableName(value = "organization", resultMap = "organizationResultMap")
@JsonIgnoreProperties(value = {"handler"})
public class Organization implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @ExcelProperty("组织名称")
    @NotBlank(message = "名称不能为空")
    @Size(min = 1, max = 32, message = "名称长度不超过32")
    private String name;

    /**
     * 代码
     */
    @ExcelProperty("组织代码")
    @NotBlank(message = "代码不能为空")
    @Size(min = 1, max = 16, message = "代码长度不超过16")
    private String code;

    /**
     * 父组织ID
     */
    @NotNull(message = "父组织ID不能为空")
    private Integer parentId;

    @TableField(exist = false)
    private List<Organization> children;

    @JsonIgnore
    @ExcelProperty("父组织名称")
    @TableField(exist = false)
    private String parent;
}
