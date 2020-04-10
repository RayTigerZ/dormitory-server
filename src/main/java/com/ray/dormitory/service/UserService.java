package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.bean.bo.RepairerOption;
import com.ray.dormitory.bean.bo.Student;
import com.ray.dormitory.bean.po.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Ray
 * @date 2019/11/23 17:06
 */
public interface UserService extends IService<User> {
    /**
     * 通过用户名获取用户基本信息
     *
     * @param account 帐号
     * @return
     */
    User getUserByAccount(String account);

    /**
     * 修改用户密码
     *
     * @param account 帐号
     * @param oldPsw  旧密码
     * @param newPsw  新密码
     * @return
     */
    boolean updatePassword(String account, String oldPsw, String newPsw);

    /**
     * 重置密码
     *
     * @param id
     * @return
     */
    boolean resetPassword(int id);

    /**
     * 根据帐号获取学生信息
     *
     * @param account 帐号
     * @return
     * @apiNote 适用于学生角色
     */
    Student getStudentInfo(String account);

    /**
     * 获取当前的登录用户
     *
     * @param request http请求
     * @return 当前登录的用户信息
     */
    User getCurrentUser(HttpServletRequest request);

    /**
     * 获取维修人员
     *
     * @return 维修人员的信息
     */
    List<RepairerOption> getRepairerOptions();
}
