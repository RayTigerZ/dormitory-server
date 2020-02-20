package com.ray.dormitory.util.kmean;

import com.baomidou.mybatisplus.core.toolkit.SerializationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Ray
 * @date : 2020.02.17 12:29
 */
public abstract class Kmeans<T extends Serializable> {

    /**
     * 所有数据
     */
    private List<T> points;

    /**
     * 点族的个数
     */
    private int size;

    /**
     * 单个点族中点的个数
     */
    private int n;

    public Kmeans(List<T> list, int size, int n) {
        this.size = size;
        this.n = n;
        points = list;
    }

    /**
     * 数据聚类
     *
     * @return 返回
     */

    public List<List<T>> clustering() {
        //初始化中心点集合
        ArrayList<T> centers = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            T point = points.get(i);
            centers.add(SerializationUtils.clone(point));
        }

        //点族集合
        List<List<T>> clusters;
        boolean result;
        do {
            //System.out.println(centers.toString());
            clusters = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                clusters.add(new ArrayList<>(size));
            }

            List<T> oldCenters = SerializationUtils.clone(centers);
            for (T point : points) {
                clustering(point, centers, clusters);
            }
            result = same(centers, oldCenters);
            //System.out.println(result);
        } while (!result);
        //System.out.println(centers);


        return clusters;
    }

    public boolean same(List<T> centers, List<T> oldCenters) {
        int len = centers.size();
        for (int i = 0; i < len; i++) {
            if (!centers.get(i).equals(oldCenters.get(i))) {
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
            if (clusters.get(i).size() < size) {
                T center = centers.get(i);
                lengths.add(getDistance(center, point));
                indexs.add(i);
            }
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

    public abstract double getDistance(T center, T point);


    /**
     * 更新点族的中心点坐标
     *
     * @param list   点族
     * @param center 中心点
     */
    public abstract void updateCenterPoint(List<T> list, T center);


}
