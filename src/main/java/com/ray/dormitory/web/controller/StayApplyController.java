package com.ray.dormitory.web.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.infrastructure.entity.StayApply;
import com.ray.dormitory.infrastructure.entity.User;
import com.ray.dormitory.web.excel.ExportData;
import com.ray.dormitory.service.StayApplyService;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * <p>
 * 留校申请 前端控制器
 * </p>
 *
 * @author Ray
 * @date 2020-2-18 19:24:33
 */
@RestController
@RequestMapping("/stayApplys")
public class StayApplyController {

    @Autowired
    private StayApplyService stayApplyService;
    @Autowired
    private UserService userService;


    @GetMapping("")
    public IPage<StayApply> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize,
                                    String student, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beginDate, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        IPage<StayApply> page = new Page<>(pageNum, pageSize);
        Wrapper<StayApply> wrapper = Wrappers.<StayApply>lambdaQuery()
                .ge(ObjectUtils.isNotNull(beginDate), StayApply::getBeginDate, beginDate)
                .le(ObjectUtils.isNotNull(endDate), StayApply::getEndDate, endDate)
                .and(StringUtils.isNotBlank(student), i -> i.like(StayApply::getStudentNum, student)
                        .or().like(StayApply::getStudentName, student)
                )
                .orderByDesc(StayApply::getCreateTime);
        return stayApplyService.page(page, wrapper);
    }

    @GetMapping("/export")
    public ExportData<StayApply> export(String student, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beginDate,
                                        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Wrapper<StayApply> wrapper = Wrappers.<StayApply>lambdaQuery()
                .ge(ObjectUtils.isNotNull(beginDate), StayApply::getBeginDate, beginDate)
                .le(ObjectUtils.isNotNull(endDate), StayApply::getEndDate, endDate)
                .and(StringUtils.isNotBlank(student), i -> i.like(StayApply::getStudentNum, student)
                        .or().like(StayApply::getStudentName, student))
                .orderByDesc(StayApply::getCreateTime);

        List<StayApply> rows = stayApplyService.list(wrapper);
        String fileName = "留校申请-" + YearMonth.now().format(DateUtils.EXPORT_FILE_DATE_FORMATTER);
        return new ExportData<>(fileName, rows);
    }


    @GetMapping("/self")
    public List<StayApply> getSelfApply(HttpServletRequest request) {
        String studentNum = userService.getCurrentUser(request).getAccount();
        Wrapper<StayApply> wrapper = Wrappers.<StayApply>lambdaQuery()
                .eq(StayApply::getStudentNum, studentNum)
                .orderByDesc(StayApply::getCreateTime);
        return stayApplyService.list(wrapper);
    }

    @PostMapping("")
    public boolean save(@Validated StayApply stayApply, HttpServletRequest request) {
        User user = userService.getCurrentUser(request);
        stayApply.setStudentNum(user.getAccount());
        stayApply.setStudentName(user.getName());
        return stayApplyService.save(stayApply);
    }

    @PostMapping("/{id}/check")
    public boolean check(@PathVariable int id, boolean result) {
        return stayApplyService.check(id, result);
    }


}
