package com.ray.dormitory.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.StayApply;
import com.ray.dormitory.service.StayApplyService;
import com.ray.dormitory.util.JwtUtil;
import com.ray.dormitory.util.SysConfig;
import com.ray.dormitory.util.bean.ExportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
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

    private static List<String> key;
    private static List<String> header;
    @Autowired
    private StayApplyService stayApplyService;
    @Autowired
    private SysConfig sysConfig;

    static {
        key = new ArrayList<>();
        key.add("studentName");
        key.add("studentNum");
        key.add("beginDate");
        key.add("endDate");
        key.add("emergencyContact");
        key.add("reason");
        key.add("remark");
        key.add("createTime");
        key.add("isConsent");
        key.add("processTime");

        header = new ArrayList<>();
        header.add("学生姓名");
        header.add("学号");
        header.add("开始时间");
        header.add("结束时间");
        header.add("紧急联系方式");
        header.add("原因");
        header.add("备注");
        header.add("申请时间");
        header.add("审核结果");
        header.add("审核时间");

    }

    @GetMapping("")
    public IPage<StayApply> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, String student, String beginDate, String endDate) {

        IPage<StayApply> page = new Page<>(pageNum, pageSize);
        Wrapper<StayApply> wrapper = Wrappers.<StayApply>lambdaQuery()
                .ge(StringUtils.isNotBlank(beginDate), StayApply::getBeginDate, beginDate)
                .le(StringUtils.isNotBlank(endDate), StayApply::getEndDate, endDate)
                .and(StringUtils.isNotBlank(student), i -> {
                    i.like(StayApply::getStudentNum, student).or().like(StayApply::getStudentName, student);
                })
                .orderByDesc(StayApply::getCreateTime);
        return stayApplyService.page(page, wrapper);
    }

    @GetMapping("/export")
    public ExportData<StayApply> export(String student, String beginDate, String endDate) {

        Wrapper<StayApply> wrapper = Wrappers.<StayApply>lambdaQuery()
                .ge(StringUtils.isNotBlank(beginDate), StayApply::getBeginDate, beginDate)
                .le(StringUtils.isNotBlank(endDate), StayApply::getEndDate, endDate)
                .and(StringUtils.isNotBlank(student), i -> {
                    i.like(StayApply::getStudentNum, student).or().like(StayApply::getStudentName, student);
                })
                .orderByDesc(StayApply::getCreateTime);

        List<StayApply> data = stayApplyService.list(wrapper);
        return new ExportData<>(header, key, data);
    }


    @GetMapping("/self")
    public List<StayApply> getSelfApply(HttpServletRequest request) {

        String studentNum = getStudentNum(request);
        Wrapper<StayApply> wrapper = Wrappers.<StayApply>lambdaQuery().eq(StayApply::getStudentNum, studentNum).orderByDesc(StayApply::getCreateTime);
        return stayApplyService.list(wrapper);
    }

    @PostMapping("")
    public boolean save(@Valid StayApply stayApply, HttpServletRequest request) {

        stayApply.setStudentNum(getStudentNum(request));
        stayApply.setStudentName(getStudentName(request));
        return stayApplyService.save(stayApply);
    }

    @PostMapping("/{id}/check")
    public boolean check(@PathVariable int id, boolean result) {

        return stayApplyService.check(id, result);

    }

    private String getStudentNum(HttpServletRequest request) {
        String token = request.getHeader(sysConfig.getTokenName());
        return JwtUtil.getAccount(token);
    }

    private String getStudentName(HttpServletRequest request) {
        String token = request.getHeader(sysConfig.getTokenName());
        return JwtUtil.getName(token);
    }


}
