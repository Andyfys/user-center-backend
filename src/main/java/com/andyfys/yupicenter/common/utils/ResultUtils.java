package com.andyfys.yupicenter.common.utils;

import com.andyfys.yupicenter.common.BaseResponse;
import com.andyfys.yupicenter.common.ErrorCode;

/**
 * 自定义返回工具类
 *
 * @author Andyfys
 * @version 1.0
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data 数据
     * @return {@link BaseResponse}<{@link T}>
     */
    public static <T> BaseResponse<T> success(T data) {
        //成功返回--0
       return new BaseResponse<>(0,data,"ok","");

    }


    /**
     * 错误返回
     *
     * @param errorCode 错误代码
     * @return {@link BaseResponse}
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 错误
     *
     * @param errorCode   错误代码
     * @param message     消息
     * @param description 描述
     * @return {@link BaseResponse}
     */
    public static BaseResponse error(ErrorCode errorCode,String message,String description) {
        return new BaseResponse<>(errorCode.getCode(),null,message,description);
    }
    public static BaseResponse error(int code,String message,String description) {
        return new BaseResponse<>(code,null,message,description);
    }
}
