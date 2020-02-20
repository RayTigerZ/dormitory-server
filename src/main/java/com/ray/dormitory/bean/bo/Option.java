package com.ray.dormitory.bean.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : Ray
 * @date : 2019.12.07 16:03
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Option {

    private String option;
    private Integer value;
    private Integer questionId;
}
