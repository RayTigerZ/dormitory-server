package com.ray.dormitory;

import org.junit.Test;

import javax.validation.constraints.NotNull;

/**
 * @author : Ray
 * @date : 2020.01.13 16:42
 */
public class NormalTest {
    @Test
    public void test() {
        @NotNull
        Integer i = null;
        System.out.println("123");
    }
}
