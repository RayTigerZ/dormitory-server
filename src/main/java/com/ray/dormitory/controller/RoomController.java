package com.ray.dormitory.controller;

import com.alibaba.excel.EasyExcel;
import com.ray.dormitory.bean.po.Room;
import com.ray.dormitory.service.RoomService;
import com.ray.dormitory.util.UploadDataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.22 16:53
 */
@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("")
    public List<Room> getFloor(int buildingId, String floor) {
        return roomService.getRoomsOfFloor(buildingId, floor);
    }

    @PostMapping("")
    public boolean saveOrUpdate(@Valid Room room) {
        return roomService.saveOrUpdate(room);
    }

    @PostMapping("/uploadBatch")
    public boolean uploadBatch(MultipartFile file, String time) throws IOException {

        EasyExcel.read(file.getInputStream(), Room.class, new UploadDataListener(roomService, time)).sheet().doRead();
        return true;

    }
}
