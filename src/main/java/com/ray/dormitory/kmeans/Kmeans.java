package com.ray.dormitory.kmeans;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kmeans聚类算法
 *
 * @author : Ray
 * @date : 2020.02.17 12:29
 */
public class Kmeans {

    /**
     * 所有数据
     */
    private List<Point> points;

    /**
     * 单个点簇中点的个数
     */
    private int size;


    private WrongPointHandler wrongPointHandler;

    public Kmeans(List<Point> list, int size) {
        this.size = size;
        this.points = list;
        this.wrongPointHandler = new DefaultWrongPointHandler();

    }

    /**
     * 数据聚类
     *
     * @return 点簇集合
     */

    public List<List<Point>> clustering() {

        //不合格的点簇集合
        List<Point> wrongCluster = new ArrayList<>();

        //合格的点集合
        List<Point> qualifiedPoints = wrongPointHandler.handleWrong(points, wrongCluster);

        //处理合格的点
        boolean flag = qualifiedPoints.size() % size == 0;
        int len = qualifiedPoints.size() / size + (flag ? 0 : 1);

        List<Point> centers = new ArrayList<>();

        for (int i = 0; i < len; i++) {
            centers.add(qualifiedPoints.get(i).clone());
        }

        List<List<Point>> qualifiedClusters;
        boolean result;
        do {
            qualifiedClusters = new ArrayList<>(len);
            for (int i = 0; i < len; i++) {
                qualifiedClusters.add(new ArrayList<>(size));
            }

            List<Point> oldCenters = new ArrayList<>();
            centers.forEach(c -> oldCenters.add(c.clone()));

            for (Point point : qualifiedPoints) {
                clustering(point, centers, qualifiedClusters);
            }
            qualifiedClusters = qualifiedClusters.stream().filter(item -> item.size() > 0).collect(Collectors.toList());
            result = same(centers, oldCenters);

        } while (!result);


        qualifiedClusters.add(wrongCluster);

        return qualifiedClusters;
    }

    public boolean same(List<Point> centers, List<Point> oldCenters) {
        int len = centers.size();
        for (int i = 0; i < len; i++) {
            if (!centers.get(i).samePosition(oldCenters.get(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 对单个数据进行聚类
     *
     * @param point    目标数据
     * @param centers  中心数据集合
     * @param clusters 聚类集合
     */
    public void clustering(Point point, List<Point> centers, List<List<Point>> clusters) {
        List<Double> distances = new ArrayList<>();
        List<Integer> indexs = new ArrayList<>();

        int len = centers.size();

        for (int i = 0; i < len; i++) {
            Point center = centers.get(i);
            double distance = getDistance(center, point);
            distances.add(distance);
            indexs.add(i);
        }

        // 找出最小值的下标
        int index = indexs.get(getIndexOfMin(distances));

        List<Point> cluster = clusters.get(index);
        cluster.add(point);
        updateCenterPoint(cluster, centers.get(index));
    }


    /**
     * 获取最小值在 list 中的下标
     *
     * @param list 距离数组
     * @return 下标
     */

    private int getIndexOfMin(List<Double> list) {
        double min = list.get(0);
        int index = 0, size = list.size();
        for (int i = 1; i < size; i++) {
            double temp = list.get(i);
            if (temp < min) {
                min = temp;
                index = i;
            }
        }
        return index;
    }

    /**
     * 获取两点之间的距离平方
     *
     * @param center 中心数据
     * @param point  普通数据
     * @return 返回数据的距离
     */

    public double getDistance(Point center, Point point) {
        double sum = 0;
        int length = center.getCoordinate().size();
        for (int i = 0; i < length; i++) {
            sum += Math.pow(center.getCoordinate().get(i) - point.getCoordinate().get(i), 2);
        }

        return sum;
    }


    /**
     * 更新点族的中心点坐标
     *
     * @param list   点族
     * @param center 中心点
     */
    public void updateCenterPoint(List<Point> list, Point center) {
        int size = center.getCoordinate().size();
        for (int i = 0; i < size; i++) {
            double sum = 0;
            int length = list.size();
            for (Point t : list) {
                sum += t.getCoordinate().get(i);
            }

            center.getCoordinate().set(i, sum / length);
        }
    }


}
