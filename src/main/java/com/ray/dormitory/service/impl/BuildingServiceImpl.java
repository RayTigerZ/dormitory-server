package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.bo.Floor;
import com.ray.dormitory.bean.po.Building;
import com.ray.dormitory.bean.po.Room;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.exception.ErrorEnum;
import com.ray.dormitory.mapper.BuildingMapper;
import com.ray.dormitory.mapper.RoomMapper;
import com.ray.dormitory.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.22 14:52
 */
@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public List<Floor> getFloors(int buildingId) {
        return baseMapper.getFloors(buildingId);
    }


    @Override
    public boolean saveOrUpdate(Building building) {
        String name = building.getName();
        Integer id = building.getId();
        Wrapper<Building> wrapper = Wrappers.<Building>lambdaQuery()
                .ne(id != null, Building::getId, id)
                .eq(Building::getName, name);
        long count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            return super.saveOrUpdate(building);
        } else {
            throw new CustomException(ErrorEnum.BUILDING_NAME_EXIST);
        }

    }

    @Override
    public boolean removeById(Serializable id) {
        if (roomMapper.selectCount(Wrappers.<Room>lambdaUpdate().eq(Room::getBuildingId, id)) == 0) {
            return super.removeById(id);
        } else {
            throw new CustomException(202, "该建筑含有宿舍记录，无法删除");
        }


    }
}
