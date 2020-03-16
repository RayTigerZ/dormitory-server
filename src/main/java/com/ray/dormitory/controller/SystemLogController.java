package com.ray.dormitory.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.SystemLog;
import com.ray.dormitory.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Ray
 * @date : 2019.11.21 16:32
 */
@RestController
@RequestMapping("/systemLogs")
public class SystemLogController {
    @Autowired
    private SystemLogService systemLogService;

    @GetMapping("")
    public IPage<SystemLog> getLogList(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, String account, String begin, String end) {

        IPage<SystemLog> page = new Page<>(pageNum, pageSize);
        Wrapper<SystemLog> wrapper = Wrappers.<SystemLog>lambdaQuery()
                .like(StringUtils.isNotBlank(account), SystemLog::getCreateUser, account)
                .ge(StringUtils.isNotBlank(begin), SystemLog::getCreateTime, begin)
                .le(StringUtils.isNotBlank(end), SystemLog::getCreateTime, end)
                .orderByDesc(SystemLog::getCreateTime);
        return systemLogService.page(page, wrapper);
    }


}
