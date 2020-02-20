package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.bean.po.RoleMenu;

/**
 * <p>
 * 角色--菜单联系表 服务类
 * </p>
 *
 * @author Ray
 * @data 2020-01-08
 */
public interface RoleMenuService extends IService<RoleMenu> {

    boolean save(int roleId, int[] menuIds);
}
