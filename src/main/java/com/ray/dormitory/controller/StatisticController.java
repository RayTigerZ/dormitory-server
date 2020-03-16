package com.ray.dormitory.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ray.dormitory.bean.po.Cost;
import com.ray.dormitory.service.CostService;
import com.ray.dormitory.service.ViolationRecordService;
import com.ray.dormitory.service.VisitRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
    public Map<String, Object> statis() {
        Map<String, Object> map = new HashMap<>(8);
        map.put("visitSum", visitRecordService.count());
        map.put("violationSum", violationRecordService.count());
        map.put("waterSum", costService.listMaps(Wrappers.<Cost>query()
                .select("charge_name", "sum(count)")
                .lambda().groupBy(Cost::getChargeName)));
        return map;
    }
}
