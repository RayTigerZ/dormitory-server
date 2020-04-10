package com.ray.dormitory.bean.bo;

import com.ray.dormitory.bean.enums.Sex;
import com.ray.dormitory.bean.po.AllocateTemp;
import com.ray.dormitory.bean.po.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author : Ray
 * @date : 2020.03.29 1:05
 */
@Getter
@Setter
@NoArgsConstructor
public class AllocateTempDetail implements Serializable {
    private String name;
    private String studentNum;
    private Sex sex;
    private String roomNum;
    private String phone;
    private String email;
    private String cla;
    private String major;
    private String college;

    public AllocateTempDetail(AllocateTemp temp, User user) {
        this.studentNum = temp.getStudentNum();
        this.roomNum = temp.getRoomNum();
        this.name = user.getName();
        this.sex = user.getSex();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.cla = user.getCla();
        this.major = user.getMajor();
        this.college = user.getCollege();
    }

}