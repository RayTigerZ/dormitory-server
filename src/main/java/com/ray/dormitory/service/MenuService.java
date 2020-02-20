package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.bean.po.Menu;

/**
 * @author : Ray
 * @date : 2019.11.21 12:45
 */
public interface MenuService extends IService<Menu> {
    /**
     * 获取用户对应的前端菜单
     *
     * @param userId
     * @return
     */
    Menu getMenuOfUser(int userId);

    /**
     * 获取系统所有的权限，包括后端API和前端菜单
     *
     * @return
     */
    Menu getAllPermission();


}
