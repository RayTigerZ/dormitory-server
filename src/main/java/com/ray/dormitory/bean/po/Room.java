package com.ray.dormitory.bean.po;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ray.dormitory.bean.bo.Student;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class Room {

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
     * 最大可住
     */
    @ExcelProperty("最大可住")
    @NotNull(message = "最大可住不能为空")
    private Integer size;

    /**
     * 宿舍楼ID
     */
    @ExcelProperty("宿舍楼")
    @NotNull(message = "宿舍楼ID不能为空")
    private Integer buildingId;

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
