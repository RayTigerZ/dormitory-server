package com.ray.dormitory.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.bo.Student;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.bean.po.UserRole;
import com.ray.dormitory.service.UserRoleService;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.util.JwtUtil;
import com.ray.dormitory.util.UploadDataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Ray
 * @date 2019/12/04 11:11
 */

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("")
    public IPage<User> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, String account, Integer roleId, Integer[] classId) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        List<Object> userIds = new ArrayList<>();
        if (roleId != null) {
            userIds = userRoleService.listObjs(Wrappers.<UserRole>lambdaQuery().select(UserRole::getUserId).eq(UserRole::getRoleId, roleId));
            if (userIds.size() == 0) {
                return page;
            }
        }

        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StringUtils.isNotBlank(account), User::getAccount, account)
                .in(roleId != null, User::getId, userIds)
                .in(classId != null, User::getClassId, classId);

        return userService.page(page, queryWrapper);
    }


    @PostMapping("/editPsw")
    public boolean editPsw(HttpServletRequest request, String oldPsw, String newPsw) {
        String token = request.getHeader("Authorization");
        String account = JwtUtil.getAccount(token);
        return userService.updatePassword(account, newPsw, oldPsw);
    }

    @PostMapping("/batchSave")
    public boolean batchSave(MultipartFile file, String time) throws IOException {
        EasyExcel.read(file.getInputStream(), User.class, new UploadDataListener(userService, time)).sheet().doRead();
        return true;
    }

    @PostMapping("")
    public boolean saveOrUpdate(@RequestBody @Valid User user) {
        return userService.saveOrUpdate(user);
    }

    @PostMapping("/{id}/enable")
    public boolean enable(@PathVariable int id) {
        return userService.update(Wrappers.<User>lambdaUpdate().eq(User::getId, id).setSql("is_usable= !is_usable"));
    }

    @PostMapping("/{id}/resetPsw")
    public boolean resetPsw(@PathVariable int id) {
        return userService.resetPassword(id);
    }

    @GetMapping("/{account}/studentInfo")
    public Student getStudentInfo(@PathVariable String account) {
        return userService.getStudentInfo(account);
    }


}
