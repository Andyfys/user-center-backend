package com.andyfys.yupicenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 基反应
 * 自定义全局相应类
 *
 * @author Andyfys
 * @version 1.0
 * @date 2022/08/14
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 2064350126809679203L;
    private int code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(int code, T data) {
        this.code = code;
        this.data = data;
        this.message = "";
    }

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data, String message,String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.data = null;
        this.message = errorCode.getMessage();
        this.description = errorCode.getDescription();
    }

}
