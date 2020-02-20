package com.ray.dormitory.util;

import com.google.gson.Gson;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

/**
 * @author : Ray
 * @date : 2020.02.16 18:38
 */
public class Kmeans {
    private static String dataPath = "D:/graduation-project/kmeans-data.json";

    public static void main(String[] args) throws IOException {
        //generateData();
        int[][] scores = readData();

        Arrays.stream(scores).forEach(i -> {
            System.out.println(Arrays.toString(i));
        });
        
    }

    private static void generateData() throws IOException {
        int row = 100, col = 4;
        int[][] scores = new int[row][col];
        Random random = new Random();
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                scores[j][i] = random.nextInt(4) + 1;
            }
        }

        Writer write = new OutputStreamWriter(new FileOutputStream(dataPath), "UTF-8");
        write.write(new Gson().toJson(scores));
        write.flush();
        write.close();


    }

    private static int[][] readData() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(dataPath)));

        StringBuffer stringBuffer = new StringBuffer();
        String temp;


        while ((temp = bufferedReader.readLine()) != null) {
            stringBuffer.append(temp);
        }
        String jsonStr = stringBuffer.toString();

        return new Gson().fromJson(jsonStr, int[][].class);

    }
}
