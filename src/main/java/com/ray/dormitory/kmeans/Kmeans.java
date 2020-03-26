package com.ray.dormitory.kmeans;

import com.baomidou.mybatisplus.core.toolkit.SerializationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kmeans聚类算法
 *
 * @author : Ray
 * @date : 2020.02.17 12:29
 */
public class Kmeans<T extends Point> {

    /**
     * 所有数据
     */
    private List<T> points;

    /**
     * 单个点簇中点的个数
     */
    private int size;


    private WrongPointHandler<T> wrongPointHandler;

    public Kmeans(List<T> list, int size) {
        this.size = size;
        this.points = list;
        this.wrongPointHandler = new DefaultWrongPointHandler<>();

    }

    /**
     * 数据聚类
     *
     * @return 点簇集合
     */

    public List<List<T>> clustering() {

        //不合格的点簇集合
        List<T> wrongCluster = new ArrayList<>();

        //合格的点集合
        List<T> qualifiedPoints = wrongPointHandler.handleWrong(points, wrongCluster);

        //处理合格的点
        boolean flag = qualifiedPoints.size() % size == 0;
        int len = qualifiedPoints.size() / size + (flag ? 0 : 1);

        ArrayList<T> centers = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            T point = qualifiedPoints.get(i);
            centers.add(SerializationUtils.clone(point));
        }

        List<List<T>> qualifiedClusters;
        boolean result;
        do {
            qualifiedClusters = new ArrayList<>(len);
            for (int i = 0; i < len; i++) {
                qualifiedClusters.add(new ArrayList<>(size));
            }

            List<T> oldCenters = SerializationUtils.clone(centers);
            for (T point : qualifiedPoints) {
                clustering(point, centers, qualifiedClusters);
            }
            qualifiedClusters = qualifiedClusters.stream().filter(item -> item.size() > 0).collect(Collectors.toList());
            result = same(centers, oldCenters);

        } while (!result);


        qualifiedClusters.add(wrongCluster);

        return qualifiedClusters;
    }

    public boolean same(List<T> centers, List<T> oldCenters) {
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
    public void clustering(T point, List<T> centers, List<List<T>> clusters) {
        List<Double> lengths = new ArrayList<>();
        List<Integer> indexs = new ArrayList<>();

        int range = centers.size();


        for (int i = 0; i < range; i++) {
            //if (clusters.get(i).size() < size) {
            T center = centers.get(i);
            lengths.add(getDistance(center, point));
            indexs.add(i);
            //}
        }

        // 找出最小值的下标
        int index = indexs.get(getIndexOfMin(lengths));

        List<T> cluster = clusters.get(index);
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

    public double getDistance(T center, T point) {
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
    public void updateCenterPoint(List<T> list, T center) {
        int size = center.getCoordinate().size();
        for (int i = 0; i < size; i++) {
            double sum = 0;
            int length = list.size();
            for (T t : list) {
                sum += t.getCoordinate().get(i);
            }

            center.getCoordinate().set(i, sum / length);
        }
    }


}
