package com.ray.dormitory.bean.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * @author : Ray
 * @date : 2019.11.21 9:36
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"handler"})
public class Student {

    private String name;
    private String studentNum;
    private String sex;
    private Integer classId;
    private String cla;
    private String college;
    private String roomNum;

}
