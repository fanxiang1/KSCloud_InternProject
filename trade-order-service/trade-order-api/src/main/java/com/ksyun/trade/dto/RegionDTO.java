package com.ksyun.trade.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegionDTO implements Serializable {

    private String code;

    private String name;
}
