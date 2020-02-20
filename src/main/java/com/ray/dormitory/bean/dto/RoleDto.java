package com.ray.dormitory.bean.dto;

import com.ray.dormitory.bean.po.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Function;

/**
 * @author : Ray
 * @date : 2020.01.10 2:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private int id;
    private String nameZh;

    private static class Mapper implements Function<Role, RoleDto> {

        @Override
        public RoleDto apply(Role role) {
            System.out.println(role.toString());
            RoleDto roleDto = new RoleDto(role.getId(), role.getNameZh());
            return roleDto;
        }
    }

    public static Function getMapper() {
        return new Mapper();
    }
}
