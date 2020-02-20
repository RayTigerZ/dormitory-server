package com.ray.dormitory.util.bean;

/**
 * @author Ray
 */

public enum ErrorEnum {

    ERROR_204(202, "请求处理异常"),
    BUILDING_NAME_EXIST(202, "楼宇名称已存在"),
    CHILDREN_ORGANIZATION_EXIST(202, "该组织含有子组织，不允许删除"),


    ERROR_201(201, "账号权限不足"),

    ERROR_500(500, "系统内部错误"),

    ERROR_302(302, "登录Token过期"),

    ERROR_301(301, "未登录或登录超时"),
    ERROR_303(303, "账号在其他地方登陆");


    private Integer errorCode;
    private String errorMsg;

    ErrorEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}