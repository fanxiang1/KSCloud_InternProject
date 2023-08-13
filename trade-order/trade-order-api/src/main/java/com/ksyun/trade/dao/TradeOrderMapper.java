package com.ksyun.trade.dao;

import com.ksyun.trade.dao.dataobject.TradeOrder;

/**
 * (TradeOrder)表数据库访问层
 *
 * @author yangpengfei
 * @since 2023-06-26 12:13:50
 */
public interface TradeOrderMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TradeOrder queryById(Integer id);

}

