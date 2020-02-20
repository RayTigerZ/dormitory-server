package com.ray.dormitory.bean.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

/**
 * @author : Ray
 * @date : 2019.11.21 8:40
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExcelUser {

    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("帐号")
    private String account;

    @ExcelProperty("手机号码")
    private String phone;
    @ExcelProperty("邮箱")
    private String email;
    @ExcelProperty("性别")
    private String sex;

    @ExcelProperty("班级")
    private String className;

    @ExcelProperty("角色")
    private String role;
}
