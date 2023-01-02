package com.ray.dormitory.web.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.bo.RoleOption;
import com.ray.dormitory.infrastructure.entity.Role;
import com.ray.dormitory.infrastructure.entity.RoleMenu;
import com.ray.dormitory.service.MenuService;
import com.ray.dormitory.service.RoleMenuService;
import com.ray.dormitory.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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
    @GetMapping("")
    public IPage<Role> getRoles(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        IPage<Role> rolePage = new Page<>(pageNum, pageSize);
        return roleService.page(rolePage);
    }


    @PostMapping("")
    public boolean saveOrUpdate(@Valid Role role) {
        return roleService.saveOrUpdate(role);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return roleService.removeById(id);
    }


    @GetMapping("/{id}/menuIds")
    public List<Integer> getRolePermissions(@PathVariable Integer id) {
        Wrapper<RoleMenu> wrapper = Wrappers.<RoleMenu>lambdaQuery()
                .select(RoleMenu::getMenuId)
                .eq(RoleMenu::getRoleId, id);
        List<Object> objects = roleMenuService.listObjs(wrapper);
        return objects.stream().map(obj -> ((Long) obj).intValue()).collect(Collectors.toList());

    }


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
