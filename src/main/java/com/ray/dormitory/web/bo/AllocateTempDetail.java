package com.ray.dormitory.web.bo;

import com.ray.dormitory.infrastructure.enums.Sex;
import com.ray.dormitory.infrastructure.entity.AllocateTemp;
import com.ray.dormitory.infrastructure.entity.User;
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
