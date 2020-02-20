package com.ray.dormitory.exception;

import com.ray.dormitory.util.bean.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : Ray
 * @date : 2019.12.03 18:04
 */
@Getter
@Setter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private int code;
    private String msg;

    public CustomException(ErrorEnum errorEnum) {
        this.code = errorEnum.getErrorCode();
        this.msg = errorEnum.getErrorMsg();
    }


}
