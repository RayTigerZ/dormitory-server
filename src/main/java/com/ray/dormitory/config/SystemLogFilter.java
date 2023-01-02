package com.ray.dormitory.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ray.dormitory.infrastructure.entity.SystemLog;
import com.ray.dormitory.service.SystemLogService;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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
public class SystemLogFilter implements HandlerInterceptor {

    @Autowired
    private SystemLogService systemLogService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MyMetaObjectHandler metaObjectHandler;

    /**
     * 前置处理
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    /**
     * 后置处理
     *
     * @param request
     * @param response
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        metaObjectHandler.setRequest(request);

        String httpMethod = request.getMethod();

        //http预请求，不进行处理
        if ("OPTIONS".equalsIgnoreCase(httpMethod)) {
            return;
        }

        //获取URI
        String requestUri = request.getRequestURI();

        if (requestUri.startsWith("/druid")) {
            return;
        }

        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long useTime = endTime - startTime;

        //返回发出请求的IP地址
        String remoteAddr = request.getRemoteAddr();

        Map<String, Object> paramMap = new HashMap<>(8);
        Enumeration paramNames = request.getParameterNames();

        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length > 0) {
                String paramValue = paramValues[0];
                paramMap.put(paramName, paramValue);
            }
        }
        String params = objectMapper.writeValueAsString(paramMap);
        String browser = ((HttpServletRequest) request).getHeader("User-Agent");
        SystemLog systemLog = new SystemLog(null, null, remoteAddr, requestUri, httpMethod, params, null, useTime, browser, null, null, null);
        systemLogService.save(systemLog);
    }
}
