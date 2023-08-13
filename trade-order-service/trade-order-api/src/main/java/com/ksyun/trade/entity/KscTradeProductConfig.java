package com.ksyun.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName ksc_trade_product_config
 */
@TableName(value ="ksc_trade_product_config")
@Data
public class KscTradeProductConfig implements Serializable {
    private Integer id;

    private String itemNo;

    private String itemName;

    private String unit;

    private Integer value;

    private Integer orderId;

    private static final long serialVersionUID = 1L;
}