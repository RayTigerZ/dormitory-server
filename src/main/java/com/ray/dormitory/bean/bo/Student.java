package com.ray.dormitory.bean.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : Ray
 * @date : 2019.11.21 9:36
 */
@Getter
@Setter
@JsonIgnoreProperties(value = {"handler"})
public class Student {

    private String name;
    private String studentNum;
    private String sex;
    @JsonIgnore
    private Integer classId;
    private String cla;
    private String college;
    private String roomNum;

}
