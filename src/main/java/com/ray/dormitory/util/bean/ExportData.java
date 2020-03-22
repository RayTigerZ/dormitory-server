package com.ray.dormitory.util.bean;

import com.ray.dormitory.util.Export;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author : Ray
 * @date : 2020.02.19 21:21
 */
@Getter
@Setter
public class ExportData<T extends Export> {
    private String fileName;
    private List<String> header;
    private List<String> key;
    private List<T> rows;


    public ExportData(String fileName, List<T> rows) {
        if (rows != null && rows.size() > 0) {
            this.header = rows.get(0).getHeader();
            this.key = rows.get(0).getKey();
            this.fileName = fileName;
            this.rows = rows;
        }

    }


}

