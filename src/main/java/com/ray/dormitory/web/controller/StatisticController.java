package com.ray.dormitory.web.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ray.dormitory.web.bo.Sum;
import com.ray.dormitory.infrastructure.enums.CycleType;
import com.ray.dormitory.infrastructure.entity.Cost;
import com.ray.dormitory.service.CostService;
import com.ray.dormitory.service.ViolationRecordService;
import com.ray.dormitory.service.VisitRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Ray
 * @date : 2020.03.09 22:38
 */
@RestController
@RequestMapping("/statistic")
public class StatisticController {
    @Autowired
    private VisitRecordService visitRecordService;
    @Autowired
    private ViolationRecordService violationRecordService;
    @Autowired
    private CostService costService;

    @GetMapping("")
    public List<Sum> statistic() {
        Map<String, Object> map = new HashMap<>(8);

        map.put("waterSum", costService.listMaps(Wrappers.<Cost>query()
                .select("charge_name", "sum(count)")
                .lambda().groupBy(Cost::getChargeName)));
        List<Sum> sums = new ArrayList<>();
        CycleType cycleType = CycleType.MONTH;
        int last = 6;
        sums.add(new Sum(visitRecordService.count(), visitRecordService.statistic(cycleType, last)));
        sums.add(new Sum(violationRecordService.count(), violationRecordService.statistic(cycleType, last)));
        sums.add(new Sum(costService.countElectric(), costService.statisticElectric(cycleType, last)));
        sums.add(new Sum(costService.countWater(), costService.statisticWater(cycleType, last)));
        return sums;
    }
}
