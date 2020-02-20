package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.bo.Student;
import com.ray.dormitory.bean.po.Role;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.bean.po.UserRole;
import com.ray.dormitory.mapper.RoleMapper;
import com.ray.dormitory.mapper.UserMapper;
import com.ray.dormitory.mapper.UserRoleMapper;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ray Z
 * @date 2019/10/27 21:54
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;


    @Override
    public User getUserByAccount(String account) {
        return baseMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, account));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePassword(String account, String oldPsw, String newPsw) {
        User user = getUserByAccount(account);
        if (user != null) {

            if (user.getPassword().equals(MD5Util.getMD5(oldPsw + user.getSalt()))) {
                String salt = MD5Util.getSalt();
                String password = MD5Util.getMD5(newPsw + salt);

                return update(Wrappers.<User>lambdaUpdate().eq(User::getId, user.getId()).set(User::getPassword, password).set(User::getSalt, salt));

            } else {
                throw new NullPointerException("原密码有误，请重新输入");
            }
        } else {
            throw new NullPointerException("账户信息有误，请重新登录");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(int id) {
        User user = baseMapper.selectById(id);
        String account = user.getAccount();
        String salt = MD5Util.getSalt();
        String password = account.substring(account.length() - 6);
        System.out.println(password);
        password = MD5Util.getMD5(password + salt);
        return update(Wrappers.<User>lambdaUpdate().set(User::getSalt, salt).set(User::getPassword, password).eq(User::getId, id));

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(User user) {

        Integer[] roleIds = user.getRoleIds();
        if (roleIds != null && roleIds.length > 0) {
            int count = baseMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getAccount, user.getAccount()));
            if (count == 0) {
                String account = user.getAccount();

                String password = MD5Util.getMD5(account.substring(account.length() - 6));
                String salt = MD5Util.getSalt();
                password = MD5Util.getMD5(password + salt);
                user.setSalt(salt);
                user.setPassword(password);

                baseMapper.insert(user);
                int userId = user.getId();
                for (int roleId : roleIds) {
                    userRoleMapper.insert(new UserRole(null, userId, roleId));
                }

            } else {
                throw new NullPointerException(user.getAccount() + "--" + user.getName() + " 帐号已存在");
            }
        }

        return true;
    }


    /**
     * 通过user的roles或者role属性获取roleId（role优先）
     *
     * @param user
     * @return
     */
    private List<Integer> getRoleId(User user) {
        List<Integer> ids = Arrays.asList(user.getRoleIds());

        if (ids != null && ids.size() > 0) {
            for (int id : ids) {

                int count = roleMapper.selectCount(new QueryWrapper<Role>().eq("id", id));
                if (count == 0) {
                    throw new NullPointerException("角色不存在");
                } else {
                    ids.add(id);
                }

            }
        } else {
            String roleStr = user.getRoles();
            if (StringUtils.isBlank(roleStr)) {
                throw new NullPointerException("角色信息不能为空");
            }
            String[] roleStrs = roleStr.split(",");
            for (String s : roleStrs) {
                QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>().eq("name_zh", s);
                Role role = roleMapper.selectOne(queryWrapper);
                if (role == null) {
                    throw new NullPointerException("角色不存在");
                } else {
                    ids.add(role.getId());
                }
            }
        }
        return ids;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(User entity) {

        int userId = entity.getId();
        Integer[] roleIds = entity.getRoleIds();
        userRoleMapper.delete(Wrappers.<UserRole>lambdaUpdate().eq(UserRole::getUserId, userId));
        for (int roleId : roleIds) {
            userRoleMapper.insert(new UserRole(null, userId, roleId));
        }
        //防止密码和盐注入
        entity.setPassword(null);
        entity.setSalt(null);
        return super.updateById(entity);
    }


    @Override
    public Student getStudentInfo(String account) {
        return baseMapper.getStudentInfo(account);
    }
}
