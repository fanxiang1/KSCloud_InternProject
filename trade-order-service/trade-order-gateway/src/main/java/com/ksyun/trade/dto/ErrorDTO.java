package com.ksyun.trade.dto;

import lombok.Data;

@Data
public class ErrorDTO {
    private String code;

    private String msg;

    private String requestId;
}
