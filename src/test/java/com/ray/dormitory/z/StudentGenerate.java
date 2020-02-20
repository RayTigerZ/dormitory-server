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
        List<ClassInfo> infos = new ArrayList<>();
        infos.add(new ClassInfo("机械工程学院", "411", "机械设计制造及其自动化", "01", 0.9));
        infos.add(new ClassInfo("机械工程学院", "411", "工业设计", "07", 0.7));
        infos.add(new ClassInfo("机械工程学院", "411", "工业工程", "08", 0.7));
        infos.add(new ClassInfo("文学与传媒学院", "423", "广播电视学", "10", 0.2));

        infos.add(new ClassInfo("文学与传媒学院", "423", "汉语言文学", "02", 0.1));
        infos.add(new ClassInfo("文学与传媒学院", "423", "文化产业管理", "12", 0.4));
        infos.add(new ClassInfo("文学与传媒学院", "423", "英语（商务英语）", "04", 0.1));

        String codePath = "D:\\Users\\Ray\\Desktop\\学院编码.xlsx";
        String nameSexPath = "D:\\Users\\Ray\\Desktop\\name&sex.xlsx";

        DataListener<NameSex> listener = new DataListener<NameSex>();
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(nameSexPath, NameSex.class, listener).sheet().doRead();
        List<NameSex> nameSexes = listener.getList();
        List<String> boyNames = new ArrayList<>(), girlNames = new ArrayList<>();
        for (NameSex nameSex : nameSexes) {
            if (nameSex.getSex().equals("男")) {
                boyNames.add(nameSex.getName());
            } else {
                girlNames.add(nameSex.getName());
            }
        }

        int minStu = 35, maxStu = 45;
        List<User> users = new ArrayList<>();
        for (int year = 2016; year <= 2019; year++) {
            for (ClassInfo info : infos) {
                for (int classNum = 1; classNum <= 2; classNum++) {
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
                            numStr = "0" + String.valueOf(num);
                        } else {
                            numStr = String.valueOf(num);
                        }
                        String phone = "13433678345", email = "13433678345@qq.com";
                        User user = new User(name, year + info.getCollegeCode() + info.getMajorCode() + classNum + numStr, phone, email, sex, cla, info.getCollege(), "学生");
                        users.add(user);
                    }

                }
            }
        }

        // 写法1
        String fileName = "D:/simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, User.class).sheet("模板").doWrite(users);


    }

    public static int getRandom(int min, int max) {
        return (int) (new Random().nextDouble() * (max - min) + min);
    }

    public static double getRandom() {
        return new Random().nextDouble();
    }
}
