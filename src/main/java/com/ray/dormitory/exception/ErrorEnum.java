package com.ray.dormitory.exception;

import lombok.Getter;

/**
 * @author Ray
 * @date 2020.01.19 21:21
 */
@Getter
public enum ErrorEnum {


    BUILDING_NAME_EXIST(202, "楼宇名称已存在"),
    CHILDREN_ORGANIZATION_EXIST(202, "该组织含有子组织，不允许删除"),

    RECORD_NOT_EXIST(202, "记录不存在"),
    CLASS_NOT_EXIST(202, "班级不存在"),

    //room
    BUILDING_PARAMETER_INCORRECT(202, "宿舍楼参数错误"),
    BUILDING_NOT_EXIST(202, "宿舍楼不存在"),
    ROOM_NUMBER_EXIST(202, "宿舍号已存在"),
    ROOM_NOT_EXIST(202, "房间不存在"),
    PARENT_NOT_EXIST(202, "父级不存在"),
    ERROR_201(201, "账号权限不足"),

    NO_LOGIN(301, "未登录或登录超时"),
    USER_ACCOUNT_NOT_UNIQUE(202, "用户账号已存在"),

    REPEAT_COST(202, "账单重复"),

    CHARGE_NOT_EXIST(202, "收费项目不存在");


    private final int errorCode;
    private final String errorMsg;

    ErrorEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}