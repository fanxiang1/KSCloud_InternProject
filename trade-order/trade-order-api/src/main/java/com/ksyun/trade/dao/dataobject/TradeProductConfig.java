package com.ksyun.trade.dao.dataobject;

import lombok.Data;

import java.io.Serializable;

/**
 * (TradeProductConfig)实体类
 *
 * @author yangpengfei
 * @since 2023-06-26 12:13:51
 */
@Data
public class TradeProductConfig implements Serializable {
    private static final long serialVersionUID = 262698471638024334L;

    private Integer id;

    private Integer orderId;

    private String itemNo;

    private String itemName;

    private String unit;

    private Integer value;

}

