package com.ray.dormitory.kmeans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Ray
 * @date : 2020.03.24 14:26
 */
public class DefaultWrongPointHandler implements WrongPointHandler {
    @Override
    public List<Point> handleWrong(List<Point> points, List<Point> wrongCluster) {
        int wrongCount = 0;
        List<Point> qualifiedPoints = new ArrayList<>();

        for (Point point : points) {
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
