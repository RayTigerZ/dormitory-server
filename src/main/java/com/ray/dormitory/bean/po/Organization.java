package com.ray.dormitory.bean.po;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ray.dormitory.valid.group.SaveByFileValid;
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
    @ExcelProperty("名称")
    @NotBlank(message = "名称不能为空")
    @Size(min = 1, max = 32, message = "名称长度不超过32")
    private String name;

    /**
     * 代码
     */
    @ExcelProperty("代码")
    @NotBlank(message = "代码不能为空")
    @Size(min = 1, max = 16, message = "代码长度不超过16")
    private String code;

    /**
     * 层级
     * 1：学院，2：专业，3：班级
     */
    @ExcelProperty("层级")
    @NotNull(message = "层级不能为空")
    private Integer level;

    /**
     * 父级ID
     */
    private Integer parentId;

    @TableField(exist = false)
    private List<Organization> children;

    @JsonIgnore
    @ExcelProperty("父级名称")
    @NotBlank(message = "父级名称不能为空", groups = {SaveByFileValid.class})
    @TableField(exist = false)
    private String parent;
}
