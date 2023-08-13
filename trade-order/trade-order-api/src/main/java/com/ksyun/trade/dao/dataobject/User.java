package com.ksyun.trade.dao.dataobject;

import lombok.Data;

import java.io.Serializable;

/**
 * (KscUser)实体类
 *
 * @author yangpengfei
 * @since 2023-06-26 12:13:49
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 397461006563992745L;

    private Integer id;

    private String username;

    private String email;

    private String phone;

    private String address;

}

