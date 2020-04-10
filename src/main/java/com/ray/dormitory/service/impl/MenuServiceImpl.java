package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Menu;
import com.ray.dormitory.mapper.MenuMapper;
import com.ray.dormitory.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author : Ray
 * @date : 2019.11.21 12:46
 */
@Service

public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {


    @Override
    public Menu getMenuOfUser(int userId) {
        List<Menu> menus = baseMapper.getMenusByUserId(userId);
        Menu menu = formatMenu(menus);

        return menu;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        remove(Wrappers.<Menu>lambdaUpdate().eq(Menu::getParentId, id));
        return super.removeById(id);
    }

    private Menu formatMenu(List<Menu> menus) {
        Map<Integer, Menu> menuMap = new HashMap<>(16);
        for (Menu menu : menus) {
            menuMap.put(menu.getId(), menu);
        }

        Iterator<Map.Entry<Integer, Menu>> it = menuMap.entrySet().iterator();
        while (it.hasNext()) {
            Menu menu = it.next().getValue();
            System.out.println(menu);
            Integer parentId = menu.getParentId();

        }

        Menu menu = menuMap.get(1);
        //除去无子树的节点，叶子节点除外

        return menu;
    }

//    void deleteNoChildNode(Menu menu) {
//        if (menu.getChildren() == null) {
//            menu = null;
//        }
//        for (Menu menu1 : menu.getChildren()) {
//            deleteNoChildNode(menu1);
//        }
//    }

    @Override
    public Menu getAllPermission() {
        List<Menu> menus = baseMapper.selectList(Wrappers.emptyWrapper());
        return formatMenu(menus);
    }

    @Override
    public List<Menu> getTree() {
        return baseMapper.selectList(Wrappers.<Menu>lambdaQuery().isNull(Menu::getParentId).orderByAsc(Menu::getOrder));
    }
}
