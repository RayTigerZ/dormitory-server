package com.ray.dormitory.z;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author : Ray
 * @date : 2019.11.26 15:14
 */
public class StudentGenerate {

    public static void main(String[] args) {

        List<ClassInfo> infos = ClassInfo.generate();
        //String codePath = "D:\\Users\\Ray\\Desktop\\学院编码.xlsx";

        String nameSexPath = "D:\\graduation-project\\name&sex.xlsx";

        DataListener<NameSex> listener = new DataListener<NameSex>();
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(nameSexPath, NameSex.class, listener).sheet().doRead();
        List<NameSex> nameSexes = listener.getList();
        List<String> boyNames = new ArrayList<>(), girlNames = new ArrayList<>();

        nameSexes.forEach(i -> (i.getSex() == "男" ? boyNames : girlNames).add(i.getName()));

        String phone = "13433678345", email = "13433678345@qq.com";
        int minStu = 45, maxStu = 55;

        List<User> users = new ArrayList<>();
        for (int year = 2020; year <= 2020; year++) {
            for (ClassInfo info : infos) {
                for (int classNum = 1; classNum <= 5; classNum++) {
                    int stuNum = getRandom(minStu, maxStu);
                    for (int num = 1; num <= stuNum; num++) {

                        String name = "";
                        String sex = "";
                        if (getRandom() <= info.getRate()) {
                            sex = "男";
                            int index = getRandom(1, boyNames.size());
                            name = boyNames.get(index);
                            boyNames.remove(index);
                        } else {
                            sex = "女";
                            int index = getRandom(1, girlNames.size());
                            name = girlNames.get(index);
                            girlNames.remove(index);
                        }
                        String numStr;
                        String cla = year + "级" + info.getMajor() + classNum + "班";
                        if (num < 10) {
                            numStr = "0" + num;
                        } else {
                            numStr = String.valueOf(num);
                        }

                        User user = new User(name, year + info.getCollegeCode() + info.getMajorCode() + classNum + numStr, phone, email, sex, cla, info.getCollege(), "学生");
                        users.add(user);
                    }

                }
            }
        }


        String fileName = "D:/student-" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, User.class).sheet("模板").doWrite(users);


    }

    public static int getRandom(int min, int max) {
        return (int) (new Random().nextDouble() * (max - min) + min);
    }

    public static double getRandom() {
        return new Random().nextDouble();
    }
}
