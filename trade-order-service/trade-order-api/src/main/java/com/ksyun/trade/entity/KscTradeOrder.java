package com.ksyun.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName ksc_trade_order
 */
@TableName(value ="ksc_trade_order")
@Data
public class KscTradeOrder implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private Integer regionId;

    /**
     * 
     */
    private Integer productId;

    /**
     * 
     */
    private BigDecimal priceValue;

    /**
     * 
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

}