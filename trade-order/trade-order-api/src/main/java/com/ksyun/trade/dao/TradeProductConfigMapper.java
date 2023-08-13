package com.ksyun.trade.dao;

import com.ksyun.trade.dao.dataobject.TradeProductConfig;

import java.util.List;

public interface TradeProductConfigMapper {

    List<TradeProductConfig> queryByOrderId(Integer orderId);
}

