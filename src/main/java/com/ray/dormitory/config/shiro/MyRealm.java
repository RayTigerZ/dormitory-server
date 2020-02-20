package com.ray.dormitory.config.shiro;


import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.mapper.MenuMapper;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.util.JwtUtil;
import com.ray.dormitory.util.bean.WebToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ray
 * @date 2019/11/23 16:10
 */
@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private MenuMapper menuMapper;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof WebToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String account = JwtUtil.getAccount(principals.toString());
        User user = userService.getUserByAccount(account);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();


        List<String> roleNames = Arrays.asList(user.getRoles().split(","));

        simpleAuthorizationInfo.addRoles(roleNames);

        simpleAuthorizationInfo.addStringPermissions(menuMapper.getApiPermissionsOfUser(user.getId()));
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();

        // 解密获得username，用于和数据库进行对比
        String account = JwtUtil.getAccount(token);
        if (account == null) {
            throw new AuthenticationException("token invalid");
        }

        User user = userService.getUserByAccount(account);
        if (user == null) {
            throw new AuthenticationException("User didn't existed!");
        }

        if (!JwtUtil.verify(token, account)) {
            throw new AuthenticationException("Username or password error");
        }

        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}