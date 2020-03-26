package com.ray.dormitory.z;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.26 17:03
 */
@Data
@AllArgsConstructor
public class ClassInfo {
    @ExcelProperty("学院")
    private String college;
    @ExcelProperty("学院编码")
    private String collegeCode;
    @ExcelProperty("专业")
    private String major;
    @ExcelProperty("专业编码")
    private String majorCode;
    @ExcelProperty("男生比例")
    private double rate;

    public static List<ClassInfo> generate() {
        List<ClassInfo> infos = new ArrayList<>();
        infos.add(new ClassInfo("机械工程学院", "411", "机械设计制造及其自动化", "01", 0.9));
        infos.add(new ClassInfo("机械工程学院", "411", "工业设计", "07", 0.7));
        infos.add(new ClassInfo("机械工程学院", "411", "工业工程", "08", 0.7));

        infos.add(new ClassInfo("文学与传媒学院", "423", "广播电视学", "10", 0.2));
        infos.add(new ClassInfo("文学与传媒学院", "423", "汉语言文学", "02", 0.1));
        infos.add(new ClassInfo("文学与传媒学院", "423", "文化产业管理", "12", 0.4));
        infos.add(new ClassInfo("文学与传媒学院", "423", "英语（商务英语）", "04", 0.1));
        return infos;
    }
}
