package com.ksyun.trade.base;

import lombok.Data;

@Data
public class Response<T> {

    private static final String SUCCESS_CODE = "200";
    private static final String SUCCESS_MSG = "ok";

    private String code;

    private String msg;

    private String requestId;

    private T data;

    public Response(){
        this.code = SUCCESS_CODE;
        this.msg = SUCCESS_MSG;
    }

    public Response(T data){
        this.data = data;
        this.code = SUCCESS_CODE;
        this.msg = SUCCESS_MSG;
    }

    public Response(String code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
