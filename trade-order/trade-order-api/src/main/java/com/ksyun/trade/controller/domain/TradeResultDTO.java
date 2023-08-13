package com.ksyun.trade.controller.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class TradeResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 订单id
     */
    private Integer id;
    /**
     * 订单金额
     */
    private BigDecimal priceValue;

    /**
     * 用户信息
     */
    private UserDTO user;
    /**
     * 机房信息
     */
    private RegionDTO region;

    /**
     * 配置信息
     */
    private List<TradeProductConfigDTO> configs;

}
