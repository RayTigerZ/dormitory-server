package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Cost;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.mapper.CostMapper;
import com.ray.dormitory.service.CostService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 账单 服务实现类
 * </p>
 *
 * @author Ray
 * @date 2020-2-26 14:56:49
 */
@Service
public class CostServiceImpl extends ServiceImpl<CostMapper, Cost> implements CostService {
    @Override
    public boolean pay(int id) {
        Cost cost = getById(id);
        if (cost == null) {
            throw new CustomException(204, "账单不存在");
        }
        if (cost.getIsPayed()) {
            throw new CustomException(204, "账单已支付，请勿重复支付");
        }
        Wrapper<Cost> wrapper = Wrappers.<Cost>lambdaUpdate()
                .set(Cost::getIsPayed, true)
                .set(Cost::getPayTime, new Date())
                .eq(Cost::getId, id);
        return update(wrapper);
    }
}
