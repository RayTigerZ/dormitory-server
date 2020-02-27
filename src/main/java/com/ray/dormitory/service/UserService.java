package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.bean.bo.Student;
import com.ray.dormitory.bean.po.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Ray
 * @date 2019/11/23 17:06
 */
public interface UserService extends IService<User> {
    /**
     * 通过用户名获取用户基本信息
     *
     * @param account
     * @return
     */
    User getUserByAccount(String account);

    /**
     * 修改用户密码
     *
     * @param account
     * @param oldPsw
     * @param newPsw
     */
    boolean updatePassword(String account, String oldPsw, String newPsw);

    boolean resetPassword(int id);

    Student getStudentInfo(String account);

    User getCurrentUser(HttpServletRequest request);

    List<Map<String, Object>> repairers();
}
