package com.ray.dormitory.kmeans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Ray
 * @date : 2020.03.24 14:26
 */
public class DefaultWrongPointHandler<T extends Point> implements WrongPointHandler<T> {
    @Override
    public List<T> handleWrong(List<T> points, List<T> wrongCluster) {
        int wrongCount = 0;
        List<T> qualifiedPoints = new ArrayList<>();

        for (T point : points) {
            if (isWrong(point)) {

                wrongCount++;
                if (wrongCount == 1 && wrongCluster == null) {
                    wrongCluster = new ArrayList<>();

                }
                wrongCluster.add(point);
            } else {
                qualifiedPoints.add(point);
            }
        }
        return qualifiedPoints;
    }
}
