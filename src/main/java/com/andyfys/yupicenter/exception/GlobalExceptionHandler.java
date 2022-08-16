package com.andyfys.yupicenter.exception;

import com.andyfys.yupicenter.common.BaseResponse;
import com.andyfys.yupicenter.common.ErrorCode;
import com.andyfys.yupicenter.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Andyfys
 * @version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("runtimeException" + e.getMessage(), e);
        //自定义异常捕捉处理器，在捕获到异常后，会执行这里
        //我们将相关的错误信息封装成一个响应对象返回给前端，防止暴露后端服务器的结构
        return ResultUtils.error(e.getCode(), e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}
