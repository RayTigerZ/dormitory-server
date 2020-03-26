package com.ray.dormitory.exception;

/**
 * @author Ray
 * @date 2020.01.19 21:21
 */

public enum ErrorEnum {

    ERROR_204(202, "请求处理异常"),
    BUILDING_NAME_EXIST(202, "楼宇名称已存在"),
    CHILDREN_ORGANIZATION_EXIST(202, "该组织含有子组织，不允许删除"),

    RECORD_NOT_EXIST(202, "记录不存在"),
    CLASS_NOT_EXIST(202, "班级不存在"),

    //room
    BUILDING_PARAMETER_INCORRECT(202, "宿舍楼参数错误"),
    BUILDING_NOT_EXIST(202, "宿舍楼不存在"),
    ROOM_NUMBER_EXIST(202, "宿舍号已存在"),

    PARENT_NOT_EXIST(202, "父级不存在"),
    ERROR_201(201, "账号权限不足"),

    ERROR_500(500, "系统内部错误"),

    ERROR_302(302, "登录Token过期"),

    ERROR_301(301, "未登录或登录超时"),
    ERROR_303(303, "账号在其他地方登陆"),
    USER_ACCOUNT_NOT_UNIQUE(202, "用户账号已存在");


    private Integer errorCode;
    private String errorMsg;

    private ErrorEnum(Integer errorCode, String errorMsg) {
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