package com.ksyun.trade.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {

    private String userName;

    private String email;

    private String phone;

    private String address;
}
