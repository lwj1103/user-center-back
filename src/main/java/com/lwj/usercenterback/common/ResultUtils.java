package com.lwj.usercenterback.common;

/**
 * 返回工具类
 */
public class ResultUtils {
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(),data,ErrorCode.SUCCESS.getMessage(),ErrorCode.SUCCESS.getDescription());
    }
    public static <T> BaseResponse<T> fail(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }
    public static <T> BaseResponse<T> fail(int code,String message,String description){
        return new BaseResponse<>(code,message,description);
    }
}
