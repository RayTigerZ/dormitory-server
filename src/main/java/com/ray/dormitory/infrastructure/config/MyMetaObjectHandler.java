package com.ray.dormitory.infrastructure.config;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ray.dormitory.infrastructure.entity.User;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.system.SysConfig;
import com.ray.dormitory.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * 在插入数据时自动填充 createUser和 createDate
 * 在更新数据时自动填充 modifyUser和 modifyDate
 *
 * @author : Ray
 * @date : 2019.12.25 16:29
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SysConfig sysConfig;
    private UserService userService;

    @Autowired
    public void setUserService(@Lazy UserService userService) {
        this.userService = userService;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void insertFill(MetaObject metaObject) {

        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());

        String authorization = request.getHeader(sysConfig.getTokenName());
        if (authorization == null) {
            return;
        }
        String account = JwtUtil.getAccount(authorization);
        if (account != null) {
            Wrapper<User> wrapper = Wrappers.<User>lambdaQuery().select(User::getName).eq(User::getAccount, account);
            User user = userService.getOne(wrapper);
            String value = user.getName() + "(" + account + ")";
            this.strictInsertFill(metaObject, "createUser", String.class, value);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {

        this.strictUpdateFill(metaObject, "modifyTime", LocalDateTime.class, LocalDateTime.now());

        String authorization = request.getHeader(sysConfig.getTokenName());
        if (authorization == null) {
            return;
        }
        String account = JwtUtil.getAccount(authorization);
        if (account != null) {
            Wrapper<User> wrapper = Wrappers.<User>lambdaQuery().select(User::getName).eq(User::getAccount, account);
            User user = userService.getOne(wrapper);
            String value = user.getName() + "(" + account + ")";
            this.strictUpdateFill(metaObject, "modifyUser", String.class, value);
        }

    }

    @Override
    public MetaObjectHandler strictFillStrategy(MetaObject metaObject, String fieldName, Supplier<?> fieldVal) {
        setFieldValByName(fieldName, fieldVal.get(), metaObject);

        return this;
    }
}
