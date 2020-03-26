package com.ray.dormitory.z;

import com.alibaba.excel.EasyExcel;
import com.ray.dormitory.bean.po.Organization;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Ray
 * @date : 2020.03.25 20:52
 */
public class ClassGenerate {
    public static void main(String[] args) {
        List<ClassInfo> infos = ClassInfo.generate();
        List<Organization> list = new ArrayList<>();

        for (ClassInfo info : infos) {
            for (int year = 2020; year <= 2020; year++) {
                for (int classNum = 1; classNum <= 5; classNum++) {
                    Organization o = new Organization();
                    o.setName(year + "级" + info.getMajor() + classNum + "班");
                    o.setParent(info.getMajor());
                    o.setLevel(3);
                    o.setCode(info.getCollegeCode() + info.getMajorCode() + year + classNum);
                    list.add(o);
                }
            }
        }

        String fileName = "D:/class-" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, Organization.class).sheet("模板").doWrite(list);

    }
}
