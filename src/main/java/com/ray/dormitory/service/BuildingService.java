package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.web.bo.Floor;
import com.ray.dormitory.infrastructure.entity.Building;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.22 14:51
 */
public interface BuildingService extends IService<Building> {
    List<Floor> getFloors(int buildingId);
}
