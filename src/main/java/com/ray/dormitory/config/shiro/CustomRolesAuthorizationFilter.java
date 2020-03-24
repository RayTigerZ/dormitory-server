package com.ray.dormitory.config.shiro;

import com.ray.dormitory.config.mvc.ResponseBean;
import com.ray.dormitory.exception.ErrorEnum;
import com.ray.dormitory.util.JwtUtil;
import com.ray.dormitory.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ray
 * @date 2019/12/05 22:09
 */
@Slf4j
public class CustomRolesAuthorizationFilter extends RolesAuthorizationFilter {

    /**
     * 判断用户是否有能够访问该url的角色，有则放行允许访问，没有则不能访问
     *
     * @param req
     * @param resp
     * @param mappedValue
     * @return
     * @throws IOException
     */
    @Override

    public boolean isAccessAllowed(ServletRequest req, ServletResponse resp, Object mappedValue)
            throws IOException {

        HttpServletRequest request = (HttpServletRequest) req;
        String authorization = request.getHeader("Authorization");
        // 获取要访问URI和method
        String uri = request.getRequestURI();
        String method = request.getMethod();


        log.info("request uri:" + uri);


        // token为空，不放行
        if (authorization == null) {
            return false;
        }

        String roles = JwtUtil.getRoles(authorization);
        List<String> roleNames = Arrays.asList(roles.split(","));


        // url所需的角色
        String[] rolesArray = (String[]) mappedValue;

        if (rolesArray == null || rolesArray.length == 0) {
            return false;
        }

        // 若当前用户拥有 rolesArray中的任何一个角色，则放行
        for (String aRolesArray : rolesArray) {
            if (roleNames.contains(aRolesArray)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        // 处理跨域问题，跨域的请求首先会发一个options类型的请求
        // options请求直接放行
        if (servletRequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        // 其余类型请求需要鉴权
        boolean isAccess = isAccessAllowed(request, response, mappedValue);
        if (isAccess) {
            // 放行
            return true;
        } else { // 不放行，向浏览器发送json
            ResponseUtil.sendJson(servletRequest, servletResponse, new ResponseBean(ErrorEnum.ERROR_201));
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
}
