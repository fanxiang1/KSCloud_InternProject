package com.ksyun.trade.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Region implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String code;

    private String name;

    private Boolean status;
}
