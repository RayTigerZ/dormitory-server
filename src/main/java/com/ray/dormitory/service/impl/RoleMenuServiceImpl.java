package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.RoleMenu;
import com.ray.dormitory.mapper.RoleMenuMapper;
import com.ray.dormitory.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色--菜单联系表 服务实现类
 * </p>
 *
 * @author Ray
 * @since 2020-01-08
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    public boolean save(int roleId, int[] menuIds) {
        baseMapper.delete(new UpdateWrapper<RoleMenu>().lambda().eq(RoleMenu::getRoleId, roleId));
        for (int menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu(null, roleId, menuId);
            baseMapper.insert(roleMenu);
        }
        return true;
    }
}
