package com.ksyun.trade.dao.dataobject;

import lombok.Data;

import java.io.Serializable;

/**
 * (KscRegion)实体类
 *
 * @author yangpengfei
 * @since 2023-06-26 12:13:49
 */
@Data
public class Region implements Serializable {
    private static final long serialVersionUID = 983061073989234892L;

    private Integer id;

    private String code;

    private String name;

    private Integer status;

}

