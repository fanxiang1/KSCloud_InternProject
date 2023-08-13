package com.ksyun.trade.dao.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (TradeOrder)实体类
 *
 * @author yangpengfei
 * @since 2023-06-26 12:13:50
 */
@Data
public class TradeOrder implements Serializable {
    private static final long serialVersionUID = -22105365140489842L;

    private Integer id;

    private Integer userId;

    private Integer regionId;

    private Integer productId;

    private BigDecimal priceValue;

    private Date createTime;

}

