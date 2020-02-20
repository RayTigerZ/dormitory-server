package com.ray.dormitory.exception;

import com.ray.dormitory.util.bean.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局处理异常，统一返回ResponseBean
 *
 * @author : Ray
 * @date : 2019.12.03 17:11
 */
@Slf4j
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler({CustomException.class})
    public ResponseBean handler(CustomException e) {
        log.error(e.getMessage());
        return new ResponseBean(e);
    }


    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseBean handlerNotFound(NoHandlerFoundException e) {

        return new ResponseBean(404, e.getMessage());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public ResponseBean handlerParameter(Exception e) {

        log.error(e.getMessage());

        return new ResponseBean(400, "请求参数错误");
    }

    @ExceptionHandler({BindException.class})
    public ResponseBean handlerBindException(BindException e) {
        log.error(e.getMessage());
        return new ResponseBean(400, e.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler({Exception.class})
    public ResponseBean handlerOther(Exception e) {
        e.printStackTrace();
        log.error("{} ---{}", e.getClass(), e.getMessage());
        return new ResponseBean(400, e.getMessage());
    }

}
