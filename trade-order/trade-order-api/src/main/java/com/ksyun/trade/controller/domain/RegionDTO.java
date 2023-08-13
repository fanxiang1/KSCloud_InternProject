package com.ksyun.trade.controller.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

}

