package com.ray.dormitory.web.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.web.bo.RepairerOption;
import com.ray.dormitory.web.bo.Student;
import com.ray.dormitory.infrastructure.entity.User;
import com.ray.dormitory.web.excel.ExportData;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.system.SysConfig;
import com.ray.dormitory.web.excel.UploadDataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


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
    private SysConfig sysConfig;


    @GetMapping("")
    public IPage<User> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, String user, Integer roleId, Integer[] classId) {
        IPage<User> page = new Page<>(pageNum, pageSize);

        Wrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .and(StringUtils.isNotBlank(user), w -> w.like(User::getAccount, user).or().like(User::getName, user))
                .inSql(roleId != null, User::getId, "SELECT user_id from user_role where role_id='" + roleId + "'")
                .in(classId != null, User::getClassId, (Object[]) classId);

        return userService.page(page, wrapper);
    }

    @GetMapping("/export")
    public ExportData<Student> export(String user, Integer roleId, Integer[] classId) {

        Wrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .and(StringUtils.isNotBlank(user), w -> w.like(User::getAccount, user).or().like(User::getName, user))
                .inSql(roleId != null, User::getId, "SELECT user_id from user_role where role_id='" + roleId + "'")
                .in(classId != null, User::getClassId, (Object[]) classId);

        List<Student> rows = userService.list(wrapper).stream().map(Student::convert).collect(Collectors.toList());
        String fileName = "学生资料";
        return new ExportData<>(fileName, rows);
    }


    @PostMapping("/editPsw")
    public boolean editPsw(HttpServletRequest request, String oldPsw, String newPsw) {
        String account = userService.getCurrentUser(request).getAccount();
        return userService.updatePassword(account, newPsw, oldPsw);
    }

    /**
     * 只能批量导入学生
     *
     * @param file    上传的文件
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/batchSave")
    public boolean batchSave(MultipartFile file, HttpServletRequest request) throws IOException {
        String token = request.getHeader(sysConfig.getTokenName());
        Assert.notNull(token, "token为空");
        EasyExcel.read(file.getInputStream(), User.class, new UploadDataListener<>(userService, token)).sheet().doRead();
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
    public List<RepairerOption> getRepairerOptions() {
        return userService.getRepairerOptions();
    }

}
