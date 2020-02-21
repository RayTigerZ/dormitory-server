package com.ray.dormitory.util.kmean;

import com.ray.dormitory.bean.po.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author : Ray
 * @date : 2020.02.17 12:23
 */
public class App {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        // 2.创建点坐标
        List<Answer> list = new ArrayList<>();

        int num = 12000, size = 4;

        Random random = new Random();
        for (int i = 0; i < num; i++) {
            int x = random.nextInt(4) + 1;
            int y = random.nextInt(4) + 1;
            int z = random.nextInt(4) + 1;
            Answer answer = new Answer();
            Double[] arr = {new Double(x), new Double(y), new Double(z)};
            answer.setAnswer(Arrays.asList(arr));
            list.add(answer);
        }

        // 执行k-means算法
        Kmeans gg = new Kmeans<Answer>(list, size, num / size) {

            @Override
            public double getDistance(Answer center, Answer point) {
                double sum = 0;
                int length = center.getAnswer().size();
                for (int i = 0; i < length; i++) {
                    sum += Math.pow(center.getAnswer().get(i) - point.getAnswer().get(i), 2);
                }

                return sum;
            }

            @Override
            public void updateCenterPoint(List<Answer> list, Answer center) {

                int size = center.getAnswer().size();
                for (int i = 0; i < size; i++) {
                    double sum = 0;
                    int length = list.size();
                    for (int j = 0; j < length; j++) {
                        sum += list.get(j).getAnswer().get(i);
                    }

                    center.getAnswer().set(i, sum / length);
                }


            }
        };


        List<List<Answer>> centers = gg.clustering();

        int n = centers.size();
        // 打印出对应的中心点 、聚类的值
        for (int i = 0; i < n; i++) {
            System.out.println("-------" + (i + 1) + "-------");

            centers.get(i).stream().forEach(point -> {
                System.out.println(point.getAnswer().toString());
            });
            System.out.println();
        }

        System.out.println(System.currentTimeMillis() - start);

    }
}
