package com.ray.dormitory.web.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.infrastructure.entity.ViolationRecord;
import com.ray.dormitory.export.ExportData;
import com.ray.dormitory.service.ViolationRecordService;
import com.ray.dormitory.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

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
    public IPage<ViolationRecord> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, String student,
                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        IPage<ViolationRecord> page = new Page<>(pageNum, pageSize);
        Wrapper<ViolationRecord> wrapper = Wrappers.<ViolationRecord>lambdaQuery()
                .like(StringUtils.isNotBlank(student), ViolationRecord::getStudentName, student)
                .or().like(StringUtils.isNotBlank(student), ViolationRecord::getStudentNum, student)
                .ge(ObjectUtils.isNotNull(beginTime), ViolationRecord::getCreateTime, beginTime)
                .le(ObjectUtils.isNotNull(endTime), ViolationRecord::getCreateTime, endTime)
                .orderByDesc(ViolationRecord::getCreateTime);
        return violationRecordService.page(page, wrapper);
    }

    @GetMapping("/export")
    public ExportData<ViolationRecord> export(String student, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        Wrapper<ViolationRecord> wrapper = Wrappers.<ViolationRecord>lambdaQuery()
                .like(StringUtils.isNotBlank(student), ViolationRecord::getStudentName, student)
                .or().like(StringUtils.isNotBlank(student), ViolationRecord::getStudentNum, student)
                .ge(ObjectUtils.isNotNull(beginTime), ViolationRecord::getCreateTime, beginTime)
                .le(ObjectUtils.isNotNull(endTime), ViolationRecord::getCreateTime, endTime)
                .orderByDesc(ViolationRecord::getCreateTime);
        List<ViolationRecord> rows = violationRecordService.list(wrapper);
        String fileName = "学生违规记录-" + YearMonth.now().format(DateUtils.EXPORT_FILE_DATE_FORMATTER);
        return new ExportData<>(fileName, rows);
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
