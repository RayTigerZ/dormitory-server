package com.ray.dormitory.web.config.mvc;


import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.exception.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ray
 * @date 2019/12/03 19:23
 * <p>
 * http response的统一返回格式
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBean {

    /**
     * 状态码
     */
    private int code = 200;

    /**
     * 返回的信息
     */
    private String msg = "操作成功";

    /**
     * 返回的数据
     */
    private Object data;

    public ResponseBean(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseBean(ErrorEnum errorEnum) {
        this.code = errorEnum.getErrorCode();
        this.msg = errorEnum.getErrorMsg();
    }

    public ResponseBean(CustomException e) {
        this.code = e.getCode();
        this.msg = e.getMsg();
    }

    public ResponseBean(Object data) {
        this.data = data;
    }


}