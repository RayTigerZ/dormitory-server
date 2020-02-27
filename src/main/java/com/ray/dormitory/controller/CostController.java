package com.ray.dormitory.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.Cost;
import com.ray.dormitory.service.CostService;
import com.ray.dormitory.service.NoticeService;
import com.ray.dormitory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 账单 前端控制器
 * </p>
 *
 * @author Ray
 * @date 2020-2-26 14:56:49
 */
@RestController
@RequestMapping("/costs")
public class CostController {
    @Autowired
    private CostService costService;
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeService noticeService;

    @GetMapping("")
    public IPage<Cost> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, String roomNum, String cycle) {
        IPage<Cost> page = new Page<>(pageNum, pageSize);
        Wrapper<Cost> wrapper = Wrappers.<Cost>lambdaQuery()
                .like(StringUtils.isNotBlank(roomNum), Cost::getRoomNum, roomNum)
                .eq(StringUtils.isNotBlank(cycle), Cost::getCycle, cycle)
                .orderByDesc(Cost::getCycle);
        return costService.page(page, wrapper);
    }

    @PostMapping("/notice")
    public boolean notice(int[] ids) {
        return noticeService.costNotice(ids);
    }

    @GetMapping("/self")
    public IPage<Cost> getSelfPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, HttpServletRequest request) {
        IPage<Cost> page = new Page<>(pageNum, pageSize);
        String roomNum = userService.getCurrentUser(request).getRoomNum();
        Wrapper<Cost> wrapper = Wrappers.<Cost>lambdaQuery()
                .eq(Cost::getRoomNum, roomNum)
                .orderByDesc(Cost::getCycle);
        return costService.page(page, wrapper);
    }

}
