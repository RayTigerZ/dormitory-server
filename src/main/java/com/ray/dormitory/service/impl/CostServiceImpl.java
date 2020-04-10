package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Charge;
import com.ray.dormitory.bean.po.Cost;
import com.ray.dormitory.bean.po.Room;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.exception.ErrorEnum;
import com.ray.dormitory.mapper.ChargeMapper;
import com.ray.dormitory.mapper.CostMapper;
import com.ray.dormitory.mapper.RoomMapper;
import com.ray.dormitory.service.CostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
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
                .set(Cost::getPayTime, new Date())
                .eq(Cost::getId, id);
        return update(wrapper);
    }

    @Override
    public boolean save(@Valid Cost cost) {
        int count = roomMapper.selectCount(Wrappers.<Room>lambdaQuery().eq(Room::getNumber, cost.getRoomNum()));
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
