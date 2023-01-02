package com.ray.dormitory.web.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ray.dormitory.infrastructure.entity.Notice;
import com.ray.dormitory.infrastructure.entity.User;
import com.ray.dormitory.service.NoticeService;
import com.ray.dormitory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 通知 前端控制器
 * </p>
 *
 * @author Ray
 * @date 2020-2-28 0:19:18
 */
@RestController
@RequestMapping("/notices")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserService userService;

    @GetMapping("")
    public List<Notice> getList(HttpServletRequest request) {
        User user = userService.getCurrentUser(request);
        Wrapper<Notice> wrapper = Wrappers.<Notice>lambdaQuery()
                .eq(Notice::getAccount, user.getAccount())
                .orderByDesc(Notice::getCreateTime);
        return noticeService.list(wrapper);
    }

    @PostMapping("/{id}/read")
    public boolean read(@PathVariable int id) {
        Wrapper<Notice> wrapper = Wrappers.<Notice>lambdaUpdate()
                .eq(Notice::getId, id)
                .set(Notice::getIsRead, true);
        return noticeService.update(wrapper);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable int id) {
        return noticeService.removeById(id);
    }
}
