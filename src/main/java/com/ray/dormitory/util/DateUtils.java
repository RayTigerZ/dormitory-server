package com.ray.dormitory.util;

import java.util.Date;

/**
 * @author : Ray
 * @date : 2020.02.19 23:39
 */
public class DateUtils {
    public static boolean compare(Date begin, Date end) {
        if (begin == null || end == null) {
            throw new NullPointerException();
        }
        return begin.getTime() <= end.getTime();
    }
}
