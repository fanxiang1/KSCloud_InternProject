package com.ksyun.trade.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConfigsDTO implements Serializable {
    private String itemNo;

    private String itemName;

    private String unit;

    private Integer value;
}
