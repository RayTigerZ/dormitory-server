package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.web.bo.Count;
import com.ray.dormitory.infrastructure.enums.CycleType;
import com.ray.dormitory.infrastructure.entity.Cost;

import java.util.List;

/**
 * <p>
 * 账单 服务类
 * </p>
 *
 * @author Ray
 * @date 2020-2-26 14:56:49
 */
public interface CostService extends IService<Cost> {
    /**
     * 支付账单
     *
     * @param id 账单ID
     * @return
     */
    boolean pay(int id);

    int countWater();

    int countElectric();

    List<Count> statisticWater(CycleType type, int last);

    List<Count> statisticElectric(CycleType type, int last);
}
