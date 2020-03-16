package com.ray.dormitory.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.bo.Student;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.bean.po.UserRole;
import com.ray.dormitory.service.UserRoleService;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.util.SysConfig;
import com.ray.dormitory.util.UploadDataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    @Autowired
    private SysConfig sysConfig;


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

        Wrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .like(StringUtils.isNotBlank(account), User::getAccount, account)
                .in(roleId != null, User::getId, userIds)
                .in(classId != null, User::getClassId, classId);

        return userService.page(page, wrapper);
    }


    @PostMapping("/editPsw")
    public boolean editPsw(HttpServletRequest request, String oldPsw, String newPsw) {
        String account = userService.getCurrentUser(request).getAccount();
        return userService.updatePassword(account, newPsw, oldPsw);
    }

    @PostMapping("/batchSave")
    public boolean batchSave(MultipartFile file, HttpServletRequest request) throws IOException {
        String token = request.getHeader(sysConfig.getTokenName());
        EasyExcel.read(file.getInputStream(), User.class, new UploadDataListener(userService, token)).sheet().doRead();
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

    @GetMapping("/roomNum")
    public String getMyRoomNum(HttpServletRequest request) {
        return userService.getCurrentUser(request).getRoomNum();
    }

    @GetMapping("/repairers")
    public List<Map<String, Object>> getRepairers() {
        return userService.repairers();
    }

}
