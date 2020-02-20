package com.ray.dormitory.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.Menu;
import com.ray.dormitory.bean.po.Role;
import com.ray.dormitory.bean.po.RoleMenu;
import com.ray.dormitory.service.MenuService;
import com.ray.dormitory.service.RoleMenuService;
import com.ray.dormitory.service.RoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/{id}/menus")
    public Map<String, Object> getRolePermissions(@PathVariable Integer id) {
        List<Object> ids = roleMenuService.listObjs(new QueryWrapper<RoleMenu>().lambda().select(RoleMenu::getMenuId).eq(RoleMenu::getRoleId, id));
        List<Menu> menus = menuService.list();
        Map<String, Object> map = new HashMap<>(2);
        map.put("ids", ids);
        map.put("menus", menus);
        return map;
    }


    @ApiOperation(value = "更新角色对应的菜单权限")
    @PostMapping("/{id}/menus")
    public boolean saveRoleMenu(@PathVariable int id, int[] ids) {
        return roleMenuService.save(id, ids);
    }


    @GetMapping("/options")
    public List<Map<String, Object>> getRoleOptions() {
        return roleService.listMaps(Wrappers.<Role>lambdaQuery().select(Role::getId, Role::getNameZh));
    }

}
