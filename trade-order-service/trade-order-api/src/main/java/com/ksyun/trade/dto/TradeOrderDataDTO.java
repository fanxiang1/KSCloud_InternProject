package com.ksyun.trade.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class TradeOrderDataDTO implements Serializable {

    private String upsteam;
    private Integer id;

    private BigDecimal priceValue;

    @JSONField(name = "user")
    private UserDTO userDTO;

    @JSONField(name = "region")
    private RegionDTO regionDTO;

    @JSONField(name = "configs")
    private List<ConfigsDTO> configsDTO;


}
