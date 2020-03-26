package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Role;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.bean.po.UserRole;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.exception.ErrorEnum;
import com.ray.dormitory.mapper.RoleMapper;
import com.ray.dormitory.mapper.UserMapper;
import com.ray.dormitory.mapper.UserRoleMapper;
import com.ray.dormitory.service.UserRoleService;
import com.ray.dormitory.util.SetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : Ray
 * @date : 2020.01.09 18:55
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public boolean save(int userId, Set<Integer> roleIds) {
        //用户ID不存在
        if (userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getId, userId)) == 0) {
            throw new CustomException(ErrorEnum.RECORD_NOT_EXIST);
        }

        //排除不存在的角色ID
        roleIds = roleIds.stream().filter(id -> roleMapper.selectCount(Wrappers.<Role>lambdaQuery().eq(Role::getId, id)) > 0).collect(Collectors.toSet());

        //判断用户的角色信息是否修改，未修改则不进行数据库操作
        Wrapper<UserRole> wrapper = Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userId).select(UserRole::getRoleId);
        Set<Integer> oldRoleIds = baseMapper.selectList(wrapper).stream().map(item -> item.getRoleId()).collect(Collectors.toSet());
        if (SetUtils.isEqualSet(roleIds, oldRoleIds)) {
            return true;
        }

        //已修改则操作数据库
        baseMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userId));
        List<UserRole> list = new ArrayList<>();
        for (int roleId : roleIds) {
            UserRole userRole = new UserRole(null, userId, roleId);
            list.add(userRole);
        }
        return saveBatch(list);
    }
}
