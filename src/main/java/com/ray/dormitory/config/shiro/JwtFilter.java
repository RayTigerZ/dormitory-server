package com.ray.dormitory.config.shiro;

import com.ray.dormitory.config.mvc.ResponseBean;
import com.ray.dormitory.exception.ErrorEnum;
import com.ray.dormitory.service.RedisService;
import com.ray.dormitory.system.SysConfig;
import com.ray.dormitory.util.JwtUtil;
import com.ray.dormitory.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT过滤器
 *
 * @author Ray Z
 * @date 2019/11/23 16:10
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    private RedisService redisService;
    private SysConfig sysConfig;

    public JwtFilter(RedisService redisService, SysConfig sysConfig) {
        this.redisService = redisService;
        this.sysConfig = sysConfig;
    }


    /**
     * 判断用户是否已经登陆。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {

        return getToken(request) != null;
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {

        String authorization = getToken(request);

        boolean result = false;

        if (authorization != null && JwtUtil.verifyToken(authorization)) {

            String account = JwtUtil.getAccount(authorization);
            //校验登陆的token是否与缓存中的token保持一致
            String key = JwtUtil.getAccountUserKey(account);
            String token = (String) redisService.get(key);
            if (authorization.equals(token)) {
                redisService.expire(key, 1800);
                result = true;
            }
        }
        return result;
    }


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean result = false;
        if (isLoginAttempt(request, response)) {
            try {
                result = executeLogin(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }

        }
        return result;

    }


    /**
     * 对跨域提供支持
     */


    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String url = req.getServletPath();
        log.info("JwtFilter preHandle url: {}", url);


        //处理跨域问题，跨域的请求首先会发一个options类型的请求

        if (req.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        boolean isAccess = isAccessAllowed(request, response, "");
        if (isAccess) {
            return true;
        } else {
            ResponseUtil.sendJson(req, res, new ResponseBean(ErrorEnum.NO_LOGIN));
            return false;
        }

    }


    @Override
    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestUri = this.getPathWithinApplication(request);

        String[] strings = path.split("::");

        if (strings.length <= 1) {
            // 普通的 URL, 正常处理
            return this.pathsMatch(strings[0], requestUri);
        } else {
            // 获取当前请求的 http method.
            String httpMethod = WebUtils.toHttp(request).getMethod().toUpperCase();

            // 匹配当前请求的 http method 与 过滤器链中的的是否一致
            return httpMethod.equals(strings[1].toUpperCase()) && this.pathsMatch(strings[0], requestUri);
        }
    }


    private String getToken(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        return httpServletRequest.getHeader(sysConfig.getTokenName());

    }

}
