package com.ray.dormitory.util;

import java.util.Collection;

/**
 * Set工具类
 *
 * @author : Ray
 * @date : 2020.03.25 0:44
 */
public class SetUtils {
    public static boolean isEqualSet(Collection<?> set1, Collection<?> set2) {
        if (set1 == set2) {
            return true;
        } else {
            return set1 != null && set2 != null && set1.size() == set2.size() && set1.containsAll(set2);
        }
    }
}
