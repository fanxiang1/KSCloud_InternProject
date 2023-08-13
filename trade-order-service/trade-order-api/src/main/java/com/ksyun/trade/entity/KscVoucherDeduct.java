package com.ksyun.trade.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @TableName ksc_voucher_deduct
 */
@TableName(value ="ksc_voucher_deduct")
@Data
public class KscVoucherDeduct implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer orderId;

    private String voucherNo;

    private BigDecimal amount;

    private BigDecimal beforeDeductAmount;

    private BigDecimal afterDeductAmount;

    @TableField(fill = FieldFill.INSERT) //创建时自动填充
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)//创建与修改时自动填充
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}