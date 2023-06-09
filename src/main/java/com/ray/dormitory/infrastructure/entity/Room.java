package com.ray.dormitory.infrastructure.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ray.dormitory.web.bo.Student;
import com.ray.dormitory.infrastructure.enums.Sex;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 宿舍房间
 * </p>
 *
 * @author : Ray
 * @date : 2019.11.22 16:49
 */
@Getter
@Setter
@TableName("room")
@JsonIgnoreProperties(value = {"handler"})
public class Room implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 宿舍号
     */
    @ExcelProperty("宿舍号")
    @Size(min = 6, max = 6, message = "宿舍号为6位数")
    private String number;

    /**
     * 类型：男/女
     */
    private Sex type;

    /**
     * 容量
     */
    @ExcelProperty("容量")
    @NotNull(message = "容量不能为空")
    private Integer size;

    /**
     * 宿舍楼ID
     */
    @NotNull(message = "宿舍楼ID不能为空")
    private Integer buildingId;

    /**
     * 宿舍楼名称
     */
    @ExcelProperty("宿舍楼")
    @TableField(exist = false)
    private String buildingName;

    /**
     * 已住人数
     */
    @TableField(exist = false)
    private Integer lived;

    /**
     * 宿舍的学生列表
     */
    @TableField(exist = false)
    private List<Student> students;

}
