package com.andyfys.yupicenter.common;

/**
 * 自定义错误码
 *
 * @author Andyfys
 * @version 1.0
 * @date 2022/08/14
 */
public enum ErrorCode {

    /**
     * 成功
     */
    SUCCESS(0,"ok",""),
    /**
     * 参数错误
     */
    PARAMS_ERROR(40000,"请求参数错误",""),
    /**
     * 空值错误
     */
    NULL_ERROR(40001,"请求数据为空",""),
    /**
     * 未登录
     */
    NOT_LOGIN(40100,"未进行登录",""),
    /**
     * 没有权限
     */
    NOT_AUTH(40101,"没有对应权限",""),
    /**
     * 系统错误
     */
    SYSTEM_ERROR(50000,"系统内部错误","");


    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
