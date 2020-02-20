package com.ray.dormitory.z;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : Ray
 * @date : 2019.11.26 18:59
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class NameSex {
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("性别")
    private String sex;

}
