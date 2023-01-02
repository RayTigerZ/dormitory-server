package com.ray.dormitory.bean.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ray.dormitory.bean.enums.Sex;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.export.Export;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生信息实体类
 *
 * @author : Ray
 * @date : 2019.11.21 9:36
 */
@Getter
@Setter
@JsonIgnoreProperties(value = {"header", "key", "classId", "handler"})
public class Student implements Export {

    private String name;
    private String studentNum;
    private Sex sex;
    private String phone;
    private String email;
    private Integer classId;
    private String cla;
    private String major;
    private String college;
    private String roomNum;


    private static List<String> key;
    private static List<String> header;

    static {
        key = new ArrayList<>();
        key.add("name");
        key.add("studentNum");
        key.add("sex");
        key.add("phone");
        key.add("email");
        key.add("cla");
        key.add("major");
        key.add("college");
        key.add("roomNum");

        header = new ArrayList<>();
        header.add("学生姓名");
        header.add("学号");
        header.add("性别");
        header.add("手机号码");
        header.add("电子邮箱");
        header.add("班级");
        header.add("专业");
        header.add("学院");
        header.add("房间号");

    }

    @Override
    public List<String> getKey() {
        return key;
    }

    @Override
    public List<String> getHeader() {
        return header;
    }

    public static Student convert(Object obj) {
        if (obj instanceof User user) {
            Student student = new Student();
            student.setName(user.getName());
            student.setStudentNum(user.getAccount());
            student.setSex(user.getSex());
            student.setPhone(user.getPhone());
            student.setEmail(user.getEmail());
            student.setCla(user.getCla());
            student.setMajor(user.getMajor());
            student.setCollege(user.getCollege());
            student.setRoomNum(user.getRoomNum());
            return student;
        }
        return null;

    }
}
