package com.ksyun.trade.base;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException{

    private String code;

    private String msg;

    public BusinessException(BusinessCodeEnum business){
        this.code = business.getCode();
        this.msg = business.getMsg();
    }

    public BusinessException(String code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
