package com.ray.dormitory.web.config.mvc;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 对Response结果的统一包装处理
 *
 * @author : Ray
 * @date : 2019.12.03 17:53
 */
@RestControllerAdvice(basePackages = "com.ray.dormitory.web.controller")
public class ResponseBeanHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        ResponseBean responseBean;
        if (body instanceof ResponseBean) {
            responseBean = (ResponseBean) body;
        } else {
            responseBean = new ResponseBean(body);
        }
        return responseBean;
    }
}
