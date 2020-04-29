package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.bo.RepairerOption;
import com.ray.dormitory.bean.bo.Student;
import com.ray.dormitory.bean.po.Organization;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.exception.ErrorEnum;
import com.ray.dormitory.mapper.OrganizationMapper;
import com.ray.dormitory.mapper.UserMapper;
import com.ray.dormitory.service.UserRoleService;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.system.SysConfig;
import com.ray.dormitory.util.JwtUtil;
import com.ray.dormitory.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ray Z
 * @date 2019/10/27 21:54
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private SysConfig sysConfig;
    @Autowired
    private OrganizationMapper organizationMapper;


    @Override
    public User getUserByAccount(String account) {
        return baseMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, account));
    }

    @Override
    public boolean updatePassword(String account, String oldPsw, String newPsw) {
        User user = getUserByAccount(account);
        if (user != null) {

            if (user.getPassword().equals(Md5Util.getMD5(oldPsw + user.getSalt()))) {
                String salt = Md5Util.getSalt();
                String password = Md5Util.getMD5(newPsw + salt);

                return update(Wrappers.<User>lambdaUpdate().eq(User::getId, user.getId()).set(User::getPassword, password).set(User::getSalt, salt));

            } else {
                throw new NullPointerException("原密码有误，请重新输入");
            }
        } else {
            throw new NullPointerException("账户信息有误，请重新登录");
        }
    }

    @Override
    public boolean resetPassword(int id) {
        User user = baseMapper.selectById(id);
        Assert.notNull(user, "");

        initPassword(user);
        Wrapper<User> wrapper = Wrappers.<User>lambdaUpdate()
                .set(User::getSalt, user.getSalt())
                .set(User::getPassword, user.getPassword())
                .eq(User::getId, id);
        return update(wrapper);

    }

    /**
     * 初始化密码和盐，登录密码取帐号后6位
     *
     * @param user 用户信息
     */
    private void initPassword(User user) {
        String account = user.getAccount();
        String password = Md5Util.getMD5(account.substring(account.length() - 6));
        String salt = Md5Util.getSalt();
        password = Md5Util.getMD5(password + salt);
        user.setSalt(salt);
        user.setPassword(password);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(User user) {
        preprocess(user);
        initPassword(user);

        return super.save(user) && userRoleService.save(user.getId(), user.getRoleIds());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(User user) {
        preprocess(user);

        //防止密码和盐注入
        user.setPassword(null);
        user.setSalt(null);
        return userRoleService.save(user.getId(), user.getRoleIds()) && super.updateById(user);
    }


    @Override
    public Student getStudentInfo(String account) {
        return Student.convert(getUserByAccount(account));
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader(sysConfig.getTokenName());
        if (token == null || !JwtUtil.verifyToken(token)) {
            throw new CustomException(ErrorEnum.NO_LOGIN);
        }
        Integer userId = JwtUtil.getId(token);
        return baseMapper.selectById(userId);
    }

    @Override
    public List<RepairerOption> getRepairerOptions() {
        Wrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .select(User::getAccount, User::getName)
                .inSql(User::getId, "select user_id from user_role where role_id=" + sysConfig.getRepairerRoleId());
        return baseMapper.selectList(wrapper).stream().map(RepairerOption::convert).collect(Collectors.toList());
    }

    public void preprocess(User user) {
        Integer id = user.getId();
        Wrapper<User> userWrapper = Wrappers.<User>lambdaQuery()
                .ne(id != null, User::getId, id)
                .eq(User::getAccount, user.getAccount());
        //判断账户唯一性
        if (baseMapper.selectCount(userWrapper) > 0) {
            throw new CustomException(ErrorEnum.USER_ACCOUNT_NOT_UNIQUE);
        }

        //处理班级
        Integer classId = user.getClassId();
        String className = user.getCla();
        if (classId == null && StringUtils.isBlank(className)) {
            throw new CustomException(ErrorEnum.CLASS_NOT_EXIST);
        } else if (classId != null) {
            Wrapper<Organization> wrapper = Wrappers.<Organization>lambdaQuery()
                    .eq(Organization::getId, classId)
                    .eq(Organization::getLevel, 3);
            if (organizationMapper.selectCount(wrapper) == 0) {
                throw new CustomException(ErrorEnum.CLASS_NOT_EXIST);
            }
        } else if (StringUtils.isNotBlank(className)) {
            Wrapper<Organization> wrapper = Wrappers.<Organization>lambdaQuery()
                    .eq(Organization::getName, className)
                    .eq(Organization::getLevel, 3);
            Organization cla = organizationMapper.selectOne(wrapper);
            if (cla == null) {
                throw new CustomException(ErrorEnum.CLASS_NOT_EXIST);
            }
            user.setClassId(cla.getId());
        }

        //处理角色,角色为空时补上学生角色
        Set<Integer> roleIds = user.getRoleIds();
        if (CollectionUtils.isEmpty(roleIds)) {
            if (roleIds == null) {
                roleIds = new HashSet<>();
            }
            roleIds.add(sysConfig.getStudentRoleId());
        }
    }


}