package com.ray.dormitory.bean.bo;

import lombok.*;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.12.07 16:07
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Question {

    private String title;

    private List<String> options;
}
