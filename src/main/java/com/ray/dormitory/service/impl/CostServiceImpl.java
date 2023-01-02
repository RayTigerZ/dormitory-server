package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.web.bo.Count;
import com.ray.dormitory.infrastructure.enums.CycleType;
import com.ray.dormitory.infrastructure.entity.Charge;
import com.ray.dormitory.infrastructure.entity.Cost;
import com.ray.dormitory.infrastructure.entity.Room;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.exception.ErrorEnum;
import com.ray.dormitory.infrastructure.mapper.ChargeMapper;
import com.ray.dormitory.infrastructure.mapper.CostMapper;
import com.ray.dormitory.infrastructure.mapper.RoomMapper;
import com.ray.dormitory.service.CostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

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
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private ChargeMapper chargeMapper;

    @Override
    public boolean pay(int id) {
        Cost cost = baseMapper.selectById(id);
        if (cost == null) {
            throw new CustomException(204, "账单不存在");
        }
        if (cost.getIsPayed()) {
            throw new CustomException(204, "账单已支付，请勿重复支付");
        }
        Wrapper<Cost> wrapper = Wrappers.<Cost>lambdaUpdate()
                .set(Cost::getIsPayed, true)
                .set(Cost::getPayTime, LocalDateTime.now())
                .eq(Cost::getId, id);
        return update(wrapper);
    }

    @Override
    public int countWater() {
        Wrapper<Cost> wrapper = Wrappers.<Cost>query().select("sum(count)").eq("charge_name", "水费");
        return ((Double) baseMapper.selectObjs(wrapper).get(0)).intValue();
    }

    @Override
    public int countElectric() {
        Wrapper<Cost> wrapper = Wrappers.<Cost>query().select("sum(count)").eq("charge_name", "电费");
        return ((Double) baseMapper.selectObjs(wrapper).get(0)).intValue();
    }

    @Override
    public List<Count> statisticWater(CycleType type, int last) {
        List<Integer> list = new ArrayList<>();
        for (int i = last; i > 0; i--) {
            list.add(i - 1);
        }
        return baseMapper.statistic("水费", type, list);
    }

    @Override
    public List<Count> statisticElectric(CycleType type, int last) {
        List<Integer> list = new ArrayList<>();
        for (int i = last; i > 0; i--) {
            list.add(i - 1);
        }
        return baseMapper.statistic("电费", type, list);
    }

    @Override
    public boolean save(@Valid Cost cost) {
        long count = roomMapper.selectCount(Wrappers.<Room>lambdaQuery().eq(Room::getNumber, cost.getRoomNum()));
        if (count == 0) {
            throw new CustomException(ErrorEnum.ROOM_NOT_EXIST);
        }
        Charge charge = chargeMapper.selectOne(Wrappers.<Charge>lambdaQuery().eq(Charge::getName, cost.getChargeName()));
        if (charge == null) {
            throw new CustomException(ErrorEnum.CHARGE_NOT_EXIST);
        }

        count = baseMapper.selectCount(Wrappers.<Cost>lambdaQuery().eq(Cost::getRoomNum, cost.getRoomNum()).eq(Cost::getChargeName, cost.getChargeName()).eq(Cost::getCycle, cost.getCycle()));
        if (count > 0) {
            throw new CustomException(ErrorEnum.REPEAT_COST);
        }
        cost.setChargePrice(charge.getPrice());
        cost.setChargeUnit(charge.getUnit());
        return super.save(cost);
    }
}
