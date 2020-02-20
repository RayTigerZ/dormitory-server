package com.ray.dormitory.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

/**
 * @author : Ray
 * @date : 2019.12.09 18:58
 */
public class FileUtil {

    public static byte[] fileToByte(String filePath) {
        byte[] data = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[1024];
            while ((len = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            data = byteArrayOutputStream.toByteArray();

            fileInputStream.close();
            byteArrayOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;


    }
}
