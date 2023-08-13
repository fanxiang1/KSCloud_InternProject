package com.ksyun.trade.base;

import lombok.Getter;
import org.slf4j.MDC;

@Getter
public enum BusinessCodeEnum {
    UNKNOWN_ERROR("500", "服务器内部错误"),
    USER_UN_LOGIN("501", "服务器内部错误"),
//    USER_NOT_FOUND("B1001",MDC.get("traceId"), "用户账号密码错误"),
//    PWD_NOT_MATCH("B1002", MDC.get("traceId"),"用户账号密码错误"),
//    TOKEN_INVALID("B1003", MDC.get("traceId"),"Token失效"),
    ;

    private final String code;
    private final String msg;


    BusinessCodeEnum(String code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException getException(){
        return new BusinessException(this.code, this.msg);
    }
}
