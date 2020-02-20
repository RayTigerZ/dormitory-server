package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.UserRole;
import com.ray.dormitory.mapper.UserRoleMapper;
import com.ray.dormitory.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * @author : Ray
 * @date : 2020.01.09 18:55
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
