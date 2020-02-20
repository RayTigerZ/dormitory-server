package com.ray.dormitory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.dormitory.bean.po.Room;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.22 22:22
 */
@Repository
public interface RoomMapper extends BaseMapper<Room> {
    /**
     * 获取指定宿舍楼和楼层的所有房间信息
     *
     * @param buildingId
     * @param floor
     * @return
     */
    List<Room> getRoomsOfFloor(int buildingId, String floor);
}
