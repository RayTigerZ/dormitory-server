package com.ray.dormitory.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.Cost;
import com.ray.dormitory.service.CostService;
import com.ray.dormitory.service.NoticeService;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.util.bean.ExportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private static List<String> key;
    private static List<String> header;
    @Autowired
    private CostService costService;
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeService noticeService;

    static {
        key = new ArrayList<>();
        key.add("roomNum");
        key.add("chargeName");
        key.add("chargePrice");
        key.add("chargeUnit");
        key.add("count");
        key.add("cycle");
        key.add("createTime");
        key.add("isPayed");
        key.add("payTime");

        header = new ArrayList<>();
        header.add("宿舍号");
        header.add("收费项目");
        header.add("单价");
        header.add("单位");
        header.add("数量");
        header.add("缴费周期");
        header.add("生成时间");
        header.add("缴费状态");
        header.add("缴费时间");

    }

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
        String fileName = "宿舍收费账单-" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        return new ExportData<>(fileName, header, key, rows);
    }

    @PostMapping("/{id}/pay")
    public boolean pay(@PathVariable int id) {
        return costService.pay(id);
    }

}
