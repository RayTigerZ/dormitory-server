package com.ray.dormitory.z;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Ray
 * @date 2019/11/23 16:21
 */
@Data
@AllArgsConstructor
public class User {

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
    private String cla;
    @ExcelProperty("学院")
    private String college;


    @ExcelProperty("角色")
    private String role;


}
