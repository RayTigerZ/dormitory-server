package com.ray.dormitory.bean.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.util.Export;
import jdk.nashorn.internal.ir.annotations.Ignore;
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
@JsonIgnoreProperties(value = {"handler"})
public class Student implements Export {

    private String name;
    private String studentNum;
    private String sex;
    @JsonIgnore
    private Integer classId;
    private String cla;
    private String major;
    private String college;
    private String roomNum;


    @Ignore
    private static List<String> key;
    @Ignore
    private static List<String> header;

    static {
        key = new ArrayList<>();
        key.add("name");
        key.add("studentNum");
        key.add("sex");
        key.add("cla");
        key.add("major");
        key.add("college");
        key.add("roomNum");

        header = new ArrayList<>();
        header.add("学生姓名");
        header.add("学号");
        header.add("性别");
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

    public static Student to(Object obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            Student student = new Student();
            student.setName(user.getName());
            student.setStudentNum(user.getAccount());
            student.setSex(user.getSex());
            student.setCla(user.getCla());
            student.setMajor(user.getMajor());
            student.setCollege(user.getCollege());
            student.setRoomNum(user.getRoomNum());
            return student;
        }
        return null;

    }
}
