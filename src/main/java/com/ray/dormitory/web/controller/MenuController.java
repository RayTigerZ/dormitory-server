package com.ray.dormitory.web.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ray.dormitory.bean.bo.MenuOption;
import com.ray.dormitory.infrastructure.entity.Menu;
import com.ray.dormitory.infrastructure.entity.Operation;
import com.ray.dormitory.service.MenuService;
import com.ray.dormitory.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Ray
 * @date : 2019.11.21 11:38
 */
@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private OperationService operationService;

    @GetMapping("")
    public List<Menu> getList() {
        return menuService.getTree();
    }

    @PostMapping("")
    public boolean addOrUpdate(@Valid Menu menu) {
        return menuService.saveOrUpdate(menu);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable int id) {
        return menuService.removeById(id);
    }

    @GetMapping("/{id}/operations")
    public List<Operation> getOperations(@PathVariable int id) {
        return operationService.list(Wrappers.<Operation>lambdaQuery().eq(Operation::getMenuId, id));
    }

    @GetMapping("/options")
    public List<MenuOption> getOptions() {
        List<Menu> menus = menuService.getTree();
        return menus.stream().map(MenuOption::convert).collect(Collectors.toList());
    }


}
