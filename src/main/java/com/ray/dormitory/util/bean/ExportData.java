package com.ray.dormitory.util.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author : Ray
 * @date : 2020.02.19 21:21
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportData<T> {
    private List<String> header;
    private List<String> key;
    private List<T> data;

}
