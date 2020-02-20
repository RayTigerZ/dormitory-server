package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Building;
import com.ray.dormitory.bean.po.Room;
import com.ray.dormitory.mapper.BuildingMapper;
import com.ray.dormitory.mapper.RoomMapper;
import com.ray.dormitory.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.22 23:41
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {
    private static String buildingSuffix = "栋";

    @Autowired
    private BuildingMapper buildingMapper;

    @Override
    public List<Room> getRoomsOfFloor(int buildingId, String floor) {
        if (floor.length() == 1) {
            floor = "0" + floor;
        }
        return baseMapper.getRoomsOfFloor(buildingId, floor);
    }

    @Override
    public boolean save(Room room) {
        int roomCount = baseMapper.selectCount(Wrappers.<Room>lambdaQuery().eq(Room::getNumber, room.getNumber()));
        if (roomCount == 0) {
            Building building = buildingMapper.selectOne(Wrappers.<Building>lambdaQuery().eq(Building::getName, room.getBuildingId() + buildingSuffix));
            if (building != null) {
                room.setBuildingId(building.getId());
                baseMapper.insert(room);
            } else {
                throw new NullPointerException("宿舍楼不存在");
            }
        } else {
            throw new NullPointerException("房间号已经存在");
        }
        return true;
    }
}
