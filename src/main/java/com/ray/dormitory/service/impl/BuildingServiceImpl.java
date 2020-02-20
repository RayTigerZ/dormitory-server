package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.bo.Floor;
import com.ray.dormitory.bean.po.Building;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.mapper.BuildingMapper;
import com.ray.dormitory.service.BuildingService;
import com.ray.dormitory.util.bean.ErrorEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.22 14:52
 */
@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {

    @Override
    public List<Floor> getFloors(int buildingId) {
        return baseMapper.getFloors(buildingId);
    }


    @Override
    public boolean saveOrUpdate(Building building) {
        String name = building.getName();
        Wrapper<Building> wrapper = Wrappers.<Building>lambdaQuery().eq(Building::getName, name);
        int count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            return super.saveOrUpdate(building);
        } else {
            throw new CustomException(ErrorEnum.BUILDING_NAME_EXIST);
        }

    }
}
