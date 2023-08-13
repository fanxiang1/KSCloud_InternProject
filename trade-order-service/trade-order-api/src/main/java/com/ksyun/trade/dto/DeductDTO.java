package com.ksyun.trade.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeductDTO implements Serializable {
    private Integer code;

    private String msg;

    private String requestId;
}
