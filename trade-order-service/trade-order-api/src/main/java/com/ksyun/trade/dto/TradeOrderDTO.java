package com.ksyun.trade.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TradeOrderDTO implements Serializable {

    private Integer code;

    private String msg;

    private String requestId;

    private String descr;

    @JSONField(name = "data")
    private TradeOrderDataDTO tradeOrderDataDTO;
}
