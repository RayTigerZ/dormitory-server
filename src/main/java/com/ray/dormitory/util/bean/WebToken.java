package com.ray.dormitory.util.bean;

import org.apache.shiro.authc.AuthenticationToken;


/**
 * @author Ray
 * @date 2019/12/05 22:52
 */
public class WebToken implements AuthenticationToken {

    /**
     * 登录凭证
     */
    private String token;

    public WebToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}