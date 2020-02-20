package com.ray.dormitory.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.ViolationRecord;
import com.ray.dormitory.service.ViolationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 违规记录表 前端控制器
 * </p>
 *
 * @author Ray
 * @date 2020-2-15 16:37:24
 */
@RestController
@RequestMapping("/violationRecords")
public class ViolationRecordController {
    @Autowired
    private ViolationRecordService violationRecordService;

    @GetMapping("")
    public IPage<ViolationRecord> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        IPage<ViolationRecord> page = new Page<>(pageNum, pageSize);
        Wrapper<ViolationRecord> wrapper = Wrappers.<ViolationRecord>lambdaQuery().orderByDesc(ViolationRecord::getCreateTime);
        return violationRecordService.page(page, wrapper);
    }

    @PostMapping("")
    public boolean saveOrUpdate(@Valid ViolationRecord violationRecord) {
        return violationRecordService.saveOrUpdate(violationRecord);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id) {
        return violationRecordService.removeById(id);
    }
}
