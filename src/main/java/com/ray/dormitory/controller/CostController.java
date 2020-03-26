package com.ray.dormitory.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.Cost;
import com.ray.dormitory.export.ExportData;
import com.ray.dormitory.service.CostService;
import com.ray.dormitory.service.NoticeService;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.system.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public IPage<Cost> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize,
                               String roomNum, String cycle, String item, Boolean payed) {
        IPage<Cost> page = new Page<>(pageNum, pageSize);
        Wrapper<Cost> wrapper = Wrappers.<Cost>lambdaQuery()
                .like(StringUtils.isNotBlank(roomNum), Cost::getRoomNum, roomNum)
                .eq(StringUtils.isNotBlank(cycle), Cost::getCycle, cycle)
                .eq(StringUtils.isNotBlank(item), Cost::getChargeName, item)
                .eq(ObjectUtils.isNotNull(payed), Cost::getIsPayed, payed)
                .orderByDesc(Cost::getCycle);
        return costService.page(page, wrapper);
    }

    @PostMapping("/notice")
    public boolean notice(int[] ids) {
        return noticeService.costNotice(ids);
    }

    /**
     * 查询用户本人房间的费用账单
     */
    @GetMapping("/room")
    public IPage<Cost> getSelfPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, HttpServletRequest request) {
        IPage<Cost> page = new Page<>(pageNum, pageSize);
        String roomNum = userService.getCurrentUser(request).getRoomNum();
        Wrapper<Cost> wrapper = Wrappers.<Cost>lambdaQuery()
                .eq(Cost::getRoomNum, roomNum)
                .orderByDesc(Cost::getCycle);
        return costService.page(page, wrapper);
    }

    @GetMapping("/export")
    public ExportData<Cost> export(String roomNum, String cycle, String item, Boolean payed) {
        Wrapper<Cost> wrapper = Wrappers.<Cost>lambdaQuery()
                .like(StringUtils.isNotBlank(roomNum), Cost::getRoomNum, roomNum)
                .eq(StringUtils.isNotBlank(cycle), Cost::getCycle, cycle)
                .eq(StringUtils.isNotBlank(item), Cost::getChargeName, item)
                .eq(ObjectUtils.isNotNull(payed), Cost::getIsPayed, payed)
                .orderByDesc(Cost::getCycle);
        List<Cost> rows = costService.list(wrapper);
        String fileName = "宿舍收费账单-" + new SimpleDateFormat(Constants.EXPORT_FILE_DATE_FORMAT).format(new Date());
        return new ExportData<>(fileName, rows);
    }

    @PostMapping("/{id}/pay")
    public boolean pay(@PathVariable int id) {
        return costService.pay(id);
    }

}
