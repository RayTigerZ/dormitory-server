package com.ray.dormitory.web.bo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author : Ray
 * @date : 2019.11.22 16:54
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Floor {

    /**
     * 楼层
     */
    private int num;

    /**
     * 已住
     */
    private int lived;

    /**
     * 空余
     */
    private int free;


}
