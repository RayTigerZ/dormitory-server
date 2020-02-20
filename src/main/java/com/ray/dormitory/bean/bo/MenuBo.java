package com.ray.dormitory.bean.bo;

import com.ray.dormitory.bean.po.Menu;
import com.ray.dormitory.bean.po.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.21 9:43
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MenuBo {

    private Integer id;
    private String url;
    private String path;
    private String component;
    private String name;
    private String iconClass;
    private int type;
    private Integer parentId;
    private boolean keepAlive;
    private boolean requireAuth;


    private List<Role> roles;

    private List<Menu> children;

    public void addChild(Menu child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }
}
