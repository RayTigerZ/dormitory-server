package com.ray.dormitory.config;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.system.SysConfig;
import com.ray.dormitory.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    @Autowired
    private UserService userService;

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void insertFill(MetaObject metaObject) {

        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());

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
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "modifyTime", Date.class, new Date());

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

    /**
     * 重写严格模式填充策略：强制覆盖值（默认为有值不覆盖）
     *
     * @param metaObject
     * @param fieldName
     * @param fieldVal
     * @return
     */

    @Override
    public MetaObjectHandler strictFillStrategy(MetaObject metaObject, String fieldName, Supplier<Object> fieldVal) {

        setFieldValByName(fieldName, fieldVal.get(), metaObject);

        return this;
    }
}
