package com.ray.dormitory.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.RepairRecord;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.service.RepairRecordService;
import com.ray.dormitory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 * 维修记录 前端控制器
 * </p>
 *
 * @author Ray
 * @date 2020-2-24 16:41:23
 */
@RestController
@RequestMapping("/repairRecords")
public class RepairRecordController {
    @Autowired
    private RepairRecordService repairRecordService;
    @Autowired
    private UserService userService;


    @PostMapping("")
    public boolean save(String problem, HttpServletRequest request) {
        String roomNum = userService.getCurrentUser(request).getRoomNum();
        RepairRecord record = new RepairRecord();
        record.setRoomNum(roomNum);
        record.setProblem(problem);
        return repairRecordService.save(record);
    }

    @GetMapping("")
    public IPage<RepairRecord> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, String user, String roomNum, Integer status) {
        IPage<RepairRecord> page = new Page<>(pageNum, pageSize);
        Wrapper<RepairRecord> wrapper = Wrappers.<RepairRecord>lambdaQuery()
                .eq(ObjectUtils.isNotNull(status), RepairRecord::getStatus, status)
                .like(StringUtils.isNotBlank(user), RepairRecord::getCreateUser, user)
                .like(StringUtils.isNotBlank(roomNum), RepairRecord::getRoomNum, roomNum)
                .orderByDesc(RepairRecord::getCreateTime);
        return repairRecordService.page(page, wrapper);
    }

    @GetMapping("/self")
    public IPage<RepairRecord> getSelfPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, HttpServletRequest request) {
        IPage<RepairRecord> page = new Page<>(pageNum, pageSize);
        String roomNum = userService.getCurrentUser(request).getRoomNum();
        Wrapper<RepairRecord> wrapper = Wrappers.<RepairRecord>lambdaQuery()
                .eq(RepairRecord::getRoomNum, roomNum).orderByDesc(RepairRecord::getCreateTime);
        return repairRecordService.page(page, wrapper);
    }

    @PostMapping("/{id}/repairer")
    public boolean setRepairer(@PathVariable int id, String account) {
        return repairRecordService.setRepairer(id, account);
    }

    @GetMapping("/work")
    public IPage<RepairRecord> getWork(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, String user, String roomNum, Integer status, HttpServletRequest request) {
        IPage<RepairRecord> page = new Page<>(pageNum, pageSize);
        Wrapper<RepairRecord> wrapper = Wrappers.<RepairRecord>lambdaQuery()
                .eq(RepairRecord::getRepairer, getRepairer(request))
                .eq(ObjectUtils.isNotNull(status), RepairRecord::getStatus, status)
                .like(StringUtils.isNotBlank(user), RepairRecord::getCreateUser, user)
                .like(StringUtils.isNotBlank(roomNum), RepairRecord::getRoomNum, roomNum)
                .orderByDesc(RepairRecord::getCreateTime);
        return repairRecordService.page(page, wrapper);
    }

    @PostMapping("/{id}/appointTime")
    public boolean setAppointTime(@PathVariable int id, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date time) {
        return repairRecordService.setAppointTime(id, time);
    }

    @PostMapping("/{id}/feedback")
    public boolean setFeedback(@PathVariable int id, String feedBack) {
        return repairRecordService.setFeedback(id, feedBack);
    }

    private String getRepairer(HttpServletRequest request) {
        User user = userService.getCurrentUser(request);
        return String.format("%s(%s)", user.getName(), user.getAccount());
    }

}
