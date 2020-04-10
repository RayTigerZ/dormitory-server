package com.ray.dormitory.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.bo.RoleOption;
import com.ray.dormitory.bean.po.Role;
import com.ray.dormitory.bean.po.RoleMenu;
import com.ray.dormitory.service.MenuService;
import com.ray.dormitory.service.RoleMenuService;
import com.ray.dormitory.service.RoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ray
 */
@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 分页获取角色列表
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    @ApiOperation(value = "分页获取角色", notes = "增加角色时参数id为空,更新角色必须传id")
    @GetMapping("")
    public IPage<Role> getRoles(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        IPage<Role> rolePage = new Page<>(pageNum, pageSize);
        return roleService.page(rolePage);
    }


    @ApiOperation(value = "增加/更新角色", notes = "增加角色时参数id为空,更新角色必须传id")
    @PostMapping("")
    public boolean saveOrUpdate(@Valid Role role) {
        return roleService.saveOrUpdate(role);
    }

    @ApiOperation(value = "删除角色", notes = "通过id删除角色")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return roleService.removeById(id);
    }


    @ApiOperation(value = "获取角色对应的菜单权限")
    @GetMapping("/{id}/menuIds")
    public List<Integer> getRolePermissions(@PathVariable Integer id) {
        Wrapper<RoleMenu> wrapper = Wrappers.<RoleMenu>lambdaQuery()
                .select(RoleMenu::getMenuId)
                .eq(RoleMenu::getRoleId, id);
        List<Object> objects = roleMenuService.listObjs(wrapper);
        return objects.stream().map(obj -> ((Long) obj).intValue()).collect(Collectors.toList());

    }


    @ApiOperation(value = "更新角色对应的菜单权限")
    @PostMapping("/{id}/menus")
    public boolean saveRoleMenu(@PathVariable int id, @RequestParam("ids") Set<Integer> ids) {
        return roleMenuService.save(id, ids);
    }


    @GetMapping("/options")
    public List<RoleOption> getRoleOptions() {
        List<Role> roles = roleService.list(Wrappers.<Role>lambdaQuery().select(Role::getId, Role::getNameZh));
        return roles.stream().map(RoleOption::convert).collect(Collectors.toList());
    }

}
