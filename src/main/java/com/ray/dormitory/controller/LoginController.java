package com.ray.dormitory.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ray.dormitory.bean.po.Role;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.config.mvc.ResponseBean;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.service.RedisService;
import com.ray.dormitory.service.RoleService;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.system.SysConfig;
import com.ray.dormitory.util.JwtUtil;
import com.ray.dormitory.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Ray
 * @date : 2020.01.07 11:43
 */
@Slf4j
@RestController
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SysConfig sysConfig;

    @PostMapping("/login")
    public ResponseBean login(String account, String password) {
        try {
            User user = userService.getUserByAccount(account);

            if (user == null) {
                throw new CustomException(204, "用户名或密码错误");
            }

            String str = password + user.getSalt();
            if (user.getPassword().equals(MD5Util.getMD5(str))) {
                if (user.getIsUsable() == null && !user.getIsUsable()) {
                    throw new UnsupportedEncodingException("该用户已被禁用");
                }

                Algorithm algorithm = Algorithm.HMAC256(JwtUtil.getSecret());
                String token = JWT.create()
                        .withClaim("id", user.getId())
                        .withClaim("account", account)
                        .withClaim("name", user.getName())
                        .withClaim("roles", user.getRoles())
                        .withClaim("loginTime", new Date())
                        .sign(algorithm);

                redisService.remove(JwtUtil.getAccountUserKey(account));
                redisService.set(JwtUtil.getAccountUserKey(account), token, sysConfig.getTokenTimeout());


                return new ResponseBean(200, "登录成功", token);
            }
            throw new CustomException(204, "用户名或密码错误");

        } catch (UnsupportedEncodingException e) {
            return new ResponseBean(204, "登录异常:" + e.getMessage());
        }

    }

    @PostMapping("/logout")
    public ResponseBean logout(HttpServletRequest servletRequest) {
        try {
            String authorization = servletRequest.getHeader("Authorization");
            String account = null;
            //校验登陆的token是否与缓存中的token保持一致
            if (authorization != null && JwtUtil.verifyToken(authorization)) {
                account = JwtUtil.getAccount(authorization);
            }
            if (account == null) {
                return new ResponseBean(202, "退出登录失败：验证失败，账号为空");
            }

            redisService.remove(JwtUtil.getAccountUserKey(account));

            return new ResponseBean(200, "退出登录成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseBean(202, "退出登录失败：" + e.getMessage());
        }
    }

    @GetMapping("/userInfo")
    public Map<String, Object> getUserInfo(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>(2);
        User user = userService.getCurrentUser(request);
        Integer userId = user.getId();
        if (userId == null) {
            throw new CustomException(202, "请重新登录");
        }
        Wrapper<Role> wrapper = Wrappers.<Role>lambdaQuery().select(Role::getName)
                .inSql(Role::getId, "select role_id from user_role where user_id=" + userId);
        List<Object> roles = roleService.listObjs(wrapper);
        map.put("roles", roles);
        map.put("user", user);
        return map;
    }

}
