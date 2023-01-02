package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.infrastructure.entity.Room;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.22 23:41
 */
public interface RoomService extends IService<Room> {
    List<Room> getRoomsOfFloor(int buildingId, String floor);
}
