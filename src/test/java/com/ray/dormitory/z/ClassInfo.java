package com.ray.dormitory.z;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : Ray
 * @date : 2019.11.26 17:03
 */
@Data
@AllArgsConstructor
public class ClassInfo {
    @ExcelProperty("学院")
    private String college;
    @ExcelProperty("学院编码")
    private String collegeCode;
    @ExcelProperty("专业")
    private String major;
    @ExcelProperty("专业编码")
    private String majorCode;
    @ExcelProperty("男生比例")
    private double rate;
}
