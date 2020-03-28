package com.ray.dormitory.kmeans;

import java.io.Serializable;
import java.util.List;

/**
 * Point接口：数据坐标
 *
 * @author : Ray
 * @date : 2020.03.23 22:15
 */
public interface Point extends Serializable {
    /**
     * 获取数据坐标
     *
     * @return 坐标
     */
    List<Double> getCoordinate();

    /**
     * 判断数据坐标是否相同
     *
     * @param point
     * @return 相同：true，不相同：false
     */
    boolean samePosition(Point point);

    Point clone();
}
