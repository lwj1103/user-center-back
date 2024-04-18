package com.lwj.usercenterback.exception;

import com.lwj.usercenterback.common.BaseResponse;
import com.lwj.usercenterback.common.ErrorCode;
import com.lwj.usercenterback.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandle(BusinessException e){
        log.info(e.getMessage());
        return ResultUtils.fail(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandle(RuntimeException e){
        log.info("runtime exception"+e);
        return new BaseResponse(ErrorCode.SYSTEM_ERROR);
    }
}
