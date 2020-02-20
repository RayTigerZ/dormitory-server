package com.ray.dormitory.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.VisitRecord;
import com.ray.dormitory.service.VisitRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 来访记录表 前端控制器
 * </p>
 *
 * @author Ray
 * @date 2020-2-14 17:23:00
 */
@RestController
@RequestMapping("/visitRecords")
public class VisitRecordController {
    @Autowired
    private VisitRecordService visitRecordService;

    @GetMapping("")
    public IPage<VisitRecord> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        IPage<VisitRecord> page = new Page<>(pageNum, pageSize);
        return visitRecordService.page(page);
    }

    @PostMapping("")
    public boolean save(@Valid VisitRecord record) {
        record.setId(null);
        return visitRecordService.save(record);
    }

    @PostMapping("/{id}/leave")
    public boolean leave(@PathVariable int id) {
        return visitRecordService.leave(id);

    }

}
