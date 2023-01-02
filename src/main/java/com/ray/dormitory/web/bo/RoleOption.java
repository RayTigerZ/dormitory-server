package com.ray.dormitory.web.bo;

import com.ray.dormitory.infrastructure.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 角色选项
 *
 * @author : Ray
 * @date : 2020.03.25 23:33
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleOption {
    private int id;
    private String name;


    public static RoleOption convert(Object object) {
        if (object instanceof Role) {
            Role role = (Role) object;
            return new RoleOption(role.getId(), role.getNameZh());
        }
        return null;
    }
}
