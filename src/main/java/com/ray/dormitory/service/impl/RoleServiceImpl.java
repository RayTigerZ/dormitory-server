package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Role;
import com.ray.dormitory.mapper.RoleMapper;
import com.ray.dormitory.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * @author Ray
 * @date 2019/11/22 23:44
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
