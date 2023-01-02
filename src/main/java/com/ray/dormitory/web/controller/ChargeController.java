package com.ray.dormitory.web.controller;


import com.ray.dormitory.infrastructure.entity.Charge;
import com.ray.dormitory.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 宿舍费用项 前端控制器
 * </p>
 *
 * @author Ray
 * @date 2020-2-26 13:07:34
 */
@RestController
@RequestMapping("/charges")
public class ChargeController {
    @Autowired
    private ChargeService chargeService;

    @GetMapping("")
    public List<Charge> getList() {
        return chargeService.list();
    }

    @PostMapping("")
    public boolean update(Charge charge) {
        return chargeService.updateById(charge);
    }

}
