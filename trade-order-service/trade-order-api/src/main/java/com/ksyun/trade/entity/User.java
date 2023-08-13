package com.ksyun.trade.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String userName;

    private String email;

    private String phone;

    private String address;
}
