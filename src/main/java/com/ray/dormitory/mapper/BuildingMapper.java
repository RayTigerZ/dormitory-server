package com.ray.dormitory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.dormitory.bean.bo.Floor;
import com.ray.dormitory.bean.po.Building;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.22 14:47
 */
@Repository
public interface BuildingMapper extends BaseMapper<Building> {

    /**
     * 获取给定宿舍楼ID的所有楼层信息
     *
     * @param buildingId
     * @return
     */
    List<Floor> getFloors(int buildingId);
}
