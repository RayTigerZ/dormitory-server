package com.ray.dormitory.bean.bo;

import com.ray.dormitory.infrastructure.entity.Menu;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Ray
 * @date : 2020.04.09 13:20
 */
@Getter
@Setter
public class MenuOption {
    private int id;
    private String title;
    private List<MenuOption> children;

    public static MenuOption convert(Object object) {
        if (object instanceof Menu) {
            Menu menu = (Menu) object;
            MenuOption option = new MenuOption();
            option.setId(menu.getId());
            option.setTitle(menu.getTitle());
            List<Menu> children = menu.getChildren();
            if (children != null && !children.isEmpty()) {
                List<MenuOption> optionChildren = new ArrayList<>();
                children.forEach(child -> optionChildren.add(convert(child)));
                option.setChildren(optionChildren);
            }

            return option;
        }
        return null;
    }

}
