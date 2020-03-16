package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.bean.po.Cost;

/**
 * <p>
 * 账单 服务类
 * </p>
 *
 * @author Ray
 * @date 2020-2-26 14:56:49
 */
public interface CostService extends IService<Cost> {

    boolean pay(int id);
}
