package com.ray.dormitory;

import com.google.gson.Gson;
import org.junit.Test;

import java.util.Date;

/**
 * @author : Ray
 * @date : 2020.01.13 16:42
 */
public class NormalTest {
    @Test
    public void test() {
        Date date = new Date();
        Date[] dates = {date, date};
        System.out.println(new Gson().toJson(dates));
        String json = "[2020-02-18,2020-02-18]";
        Date[] dates1 = new Gson().fromJson(json, Date[].class);
        System.out.println(dates1);
    }
}
