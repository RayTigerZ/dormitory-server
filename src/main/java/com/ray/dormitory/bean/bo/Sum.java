package com.ray.dormitory.bean.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author : Ray
 * @date : 2020.04.25 20:37
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sum {
    private long sum;
    private List<Count> counts;
}
