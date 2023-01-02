package com.ray.dormitory.web.excel;

import java.util.List;

/**
 * 数据导出接口
 *
 * @author : Ray
 * @date : 2020.03.20 17:09
 */
public interface Export {

    /**
     * 获取key
     *
     * @return key
     */
    List<String> getKey();

    /**
     * 获取表头
     *
     * @return header
     */
    List<String> getHeader();

}
