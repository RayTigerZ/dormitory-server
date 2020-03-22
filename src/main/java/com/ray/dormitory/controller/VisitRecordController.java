package com.ray.dormitory.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.VisitRecord;
import com.ray.dormitory.service.VisitRecordService;
import com.ray.dormitory.util.Constants;
import com.ray.dormitory.util.bean.ExportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public IPage<VisitRecord> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, String visitor,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {
        IPage<VisitRecord> page = new Page<>(pageNum, pageSize);
        Wrapper<VisitRecord> wrapper = Wrappers.<VisitRecord>lambdaQuery()
                .ge(ObjectUtils.isNotNull(beginTime), VisitRecord::getVisitTime, beginTime)
                .le(ObjectUtils.isNotNull(endTime), VisitRecord::getLeaveTime, endTime)
                .and(StringUtils.isNotBlank(visitor), i -> i.like(VisitRecord::getTrueName, visitor)
                        .or().like(VisitRecord::getIdentity, visitor))
                .orderByDesc(VisitRecord::getVisitTime);
        return visitRecordService.page(page, wrapper);
    }

    @GetMapping("/export")
    public ExportData<VisitRecord> export(String visitor, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beginTime,
                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        Wrapper<VisitRecord> wrapper = Wrappers.<VisitRecord>lambdaQuery()
                .select(VisitRecord::getTrueName, VisitRecord::getIdentity, VisitRecord::getVisitTime, VisitRecord::getLeaveTime, VisitRecord::getRemark)
                .ge(ObjectUtils.isNotNull(beginTime), VisitRecord::getVisitTime, beginTime)
                .le(ObjectUtils.isNotNull(endTime), VisitRecord::getLeaveTime, endTime)
                .and(StringUtils.isNotBlank(visitor), i -> i.like(VisitRecord::getTrueName, visitor)
                        .or().like(VisitRecord::getIdentity, visitor))
                .orderByDesc(VisitRecord::getVisitTime);

        List<VisitRecord> rows = visitRecordService.list(wrapper);
        String fileName = "访客记录-" + new SimpleDateFormat(Constants.EXPORT_FILE_DATE_FORMAT).format(new Date());
        return new ExportData<>(fileName, rows);
    }

    @PostMapping("")
    public boolean save(@Valid VisitRecord record) {
        return visitRecordService.save(record);
    }

    @PostMapping("/{id}/leave")
    public boolean leave(@PathVariable int id) {
        return visitRecordService.leave(id);

    }

}
