package com.ray.dormitory.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author : Ray
 * @date : 2019.12.04 22:15
 */
@Slf4j
public class RestPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {

    @Override
    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }

        String requestURI = getPathWithinApplication(request);
        String method = ((HttpServletRequest) request).getMethod().toUpperCase();

        for (String pathPattern : filterChainManager.getChainNames()) {
            String pattern = null;
            if (pathPattern.contains("::")) {
                String[] pathPatternArray = pathPattern.split("::");
                if (method.equalsIgnoreCase(pathPatternArray[1])) {
                    pattern = pathPatternArray[0];
                }
            } else {
                pattern = pathPattern;
            }


            // 只用过滤器链的 URL 部分与请求的 URL 进行匹配
            if (pattern != null && pathMatches(pattern, requestURI)) {
                log.info(" [{}] match url [{} {}]", pathPattern, method, requestURI);
                return filterChainManager.proxy(originalChain, pathPattern);
            }
        }

        return null;
    }


}
