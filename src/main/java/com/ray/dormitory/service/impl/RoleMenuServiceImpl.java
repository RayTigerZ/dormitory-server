package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Menu;
import com.ray.dormitory.bean.po.Role;
import com.ray.dormitory.bean.po.RoleMenu;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.exception.ErrorEnum;
import com.ray.dormitory.mapper.MenuMapper;
import com.ray.dormitory.mapper.RoleMapper;
import com.ray.dormitory.mapper.RoleMenuMapper;
import com.ray.dormitory.service.RoleMenuService;
import com.ray.dormitory.util.SetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public boolean save(int roleId, Set<Integer> menuIds) {
        //角色ID不存在
        if (roleMapper.selectCount(Wrappers.<Role>lambdaQuery().eq(Role::getId, roleId)) == 0) {
            throw new CustomException(ErrorEnum.RECORD_NOT_EXIST);
        }
        //排除不存在的菜单ID
        menuIds = menuIds.stream().filter(id -> menuMapper.selectCount(Wrappers.<Menu>lambdaQuery().eq(Menu::getId, id)) > 0).collect(Collectors.toSet());

        //判断角色的菜单权限是否修改，未修改则不进行数据库操作
        Wrapper<RoleMenu> wrapper = Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, roleId).select(RoleMenu::getMenuId);
        Set<Integer> oldMenuIds = baseMapper.selectList(wrapper).stream().map(item -> item.getMenuId()).collect(Collectors.toSet());
        if (SetUtils.isEqualSet(menuIds, oldMenuIds)) {
            return true;
        }

        //已修改则操作数据库
        baseMapper.delete(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, roleId));
        List<RoleMenu> list = new ArrayList<>();
        for (int menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu(null, roleId, menuId);
            list.add(roleMenu);
        }
        return saveBatch(list);

    }
}
