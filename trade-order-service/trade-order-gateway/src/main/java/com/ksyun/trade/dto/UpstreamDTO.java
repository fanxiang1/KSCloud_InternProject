package com.ksyun.trade.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpstreamDTO {
    private String code;

    private String msg;

    private String requestId;

    private String[] data;
}
