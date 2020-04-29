package com.ray.dormitory.controller;

import com.alibaba.excel.EasyExcel;
import com.ray.dormitory.bean.po.Room;
import com.ray.dormitory.service.RoomService;
import com.ray.dormitory.system.SysConfig;
import com.ray.dormitory.upload.UploadDataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private SysConfig sysConfig;

    @GetMapping("")
    public List<Room> getFloor(int buildingId, String floor) {
        return roomService.getRoomsOfFloor(buildingId, floor);
    }

    @PostMapping("")
    public boolean saveOrUpdate(@Valid Room room) {
        return roomService.saveOrUpdate(room);
    }

    @PostMapping("/batchSave")
    public boolean batchSave(MultipartFile file, HttpServletRequest request) throws IOException {
        String token = request.getHeader(sysConfig.getTokenName());
        Assert.notNull(token, "token为空");

        EasyExcel.read(file.getInputStream(), Room.class, new UploadDataListener(roomService, token)).sheet().doRead();
        return true;
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable int id) {
        return roomService.removeById(id);
    }


}
