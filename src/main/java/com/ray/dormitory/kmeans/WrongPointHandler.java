package com.ray.dormitory.kmeans;

import java.util.List;

/**
 * 不合格point的处理器接口
 *
 * @author : Ray
 * @date : 2020.03.24 14:12
 */
public interface WrongPointHandler<T extends Point> {

    /**
     * 判断不合格的点
     *
     * @param point
     * @return 合格：true，不合格：false
     */
    default boolean isWrong(T point) {
        return point.getCoordinate() == null;
    }

    /**
     * 处理不合格的point
     *
     * @param points：点集合
     * @param wrongCluster：不合格的点簇集合
     * @return 合格的point集合
     */
    List<T> handleWrong(List<T> points, List<T> wrongCluster);

}
