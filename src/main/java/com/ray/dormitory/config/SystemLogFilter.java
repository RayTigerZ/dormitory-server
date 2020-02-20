package com.ray.dormitory.config;


import com.google.gson.Gson;
import com.ray.dormitory.bean.po.SystemLog;
import com.ray.dormitory.service.SystemLogService;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Aop Filter，用于记录请求的内容及时间等
 * 会在 请求开始前 和 结束后 进行操作
 *
 * @author Ray
 * @date 2019/11/23 15:15
 */

@Component
public class SystemLogFilter extends AdviceFilter {

    @Autowired
    private SystemLogService systemLogService;
    @Autowired
    private MyMetaObjectHandler metaObjectHandler;


    /**
     * 前置处理
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     */
    @Override
    protected boolean preHandle(ServletRequest servletRequest, ServletResponse servletResponse) {
        servletRequest.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    /**
     * 后置处理
     *
     * @param request
     * @param response
     */
    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        metaObjectHandler.setRequest(httpServletRequest);

        String httpMethod = httpServletRequest.getMethod();

        //http预请求，不进行处理
        if ("OPTIONS".equalsIgnoreCase(httpMethod)) {
            return;
        }

        //获取URI
        String requestUri = httpServletRequest.getRequestURI();

        if (requestUri.startsWith("/druid")) {
            return;
        }

        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long useTime = endTime - startTime;

        //返回发出请求的IP地址
        String remoteAddr = request.getRemoteAddr();

        Map<String, Object> paramMap = new HashMap<>(8);
        Enumeration paramNames = httpServletRequest.getParameterNames();

        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = httpServletRequest.getParameterValues(paramName);
            if (paramValues.length > 0) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    paramMap.put(paramName, paramValue);

                }
            }
        }
        String params = new Gson().toJson(paramMap);
        String browser = ((HttpServletRequest) request).getHeader("User-Agent");
        SystemLog systemLog = new SystemLog(null, null, remoteAddr, requestUri, httpMethod, params, null, useTime, browser, null, null, null);
        systemLogService.save(systemLog);
    }
}
