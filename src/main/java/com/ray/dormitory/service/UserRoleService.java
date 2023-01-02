package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.infrastructure.entity.UserRole;

import java.util.Set;

/**
 * @author : Ray
 * @date : 2020.01.09 18:54
 */
public interface UserRoleService extends IService<UserRole> {

    boolean save(int userId, Set<Integer> roleIds);
}
