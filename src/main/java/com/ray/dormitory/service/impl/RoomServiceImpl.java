package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Building;
import com.ray.dormitory.bean.po.Room;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.exception.ErrorEnum;
import com.ray.dormitory.mapper.BuildingMapper;
import com.ray.dormitory.mapper.RoomMapper;
import com.ray.dormitory.mapper.UserMapper;
import com.ray.dormitory.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.22 23:41
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    @Autowired
    private BuildingMapper buildingMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Room> getRoomsOfFloor(int buildingId, String floor) {
        if (floor.length() == 1) {
            floor = "0" + floor;
        }
        return baseMapper.getRoomsOfFloor(buildingId, floor);
    }

    @Override
    public boolean save(Room room) {
        preprocess(room);
        return super.save(room);
    }

    @Override
    public boolean updateById(Room room) {
        preprocess(room);
        return super.updateById(room);
    }

    /**
     * 对room的数据完整性等进行检验
     *
     * @param room
     */
    private void preprocess(Room room) {
        Integer buildingId = room.getBuildingId();
        String buildingName = room.getBuildingName();
        if (buildingId == null && StringUtils.isBlank(buildingName)) {
            throw new CustomException(ErrorEnum.BUILDING_PARAMETER_INCORRECT);
        }

        if (buildingId != null) {
            Building building = buildingMapper.selectById(buildingId);
            if (building == null) {
                throw new CustomException(ErrorEnum.BUILDING_NOT_EXIST);
            }
        } else if (StringUtils.isNotBlank(buildingName)) {
            Building building = buildingMapper.selectOne(Wrappers.<Building>lambdaQuery().eq(Building::getName, buildingName));
            if (building == null) {
                throw new CustomException(ErrorEnum.BUILDING_NOT_EXIST);
            }
            room.setBuildingId(building.getId());
        }

        Integer id = room.getId();
        Wrapper<Room> wrapper = Wrappers.<Room>lambdaQuery()
                .ne(id != null, Room::getId, id)
                .eq(Room::getNumber, room.getNumber());
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new CustomException(ErrorEnum.ROOM_NUMBER_EXIST);
        }
    }


    @Override
    public boolean removeById(Serializable id) {
        Room room = baseMapper.selectById(id);
        if (room == null) {
            throw new CustomException(ErrorEnum.RECORD_NOT_EXIST);
        }
        int count = userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getRoomNum, room.getNumber()));
        if (count > 0) {
            throw new CustomException(202, "有学生的宿舍无法删除");
        }
        return super.removeById(id);
    }
}
