package com.ksyun.trade.controller.online;

import com.ksyun.trade.annotation.RateLimit;
import com.ksyun.trade.base.BusinessException;
import com.ksyun.trade.dto.UpstreamDTO;
import com.ksyun.trade.dto.VoucherDeductDTO;
import com.ksyun.trade.service.GatewayService;
import com.ksyun.trade.service.ListUpstreamInfoService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class GatewayController {
    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private ListUpstreamInfoService listUpstreamInfoService;

    /**
     * 查询订单详情 (GET)
     */
    @RequestMapping(value = "/online/queryOrderInfo", produces = "application/json")
    public Object queryOrderInfo(Integer id) {
        return gatewayService.loadLalancing(id);
    }

    /**
     * 根据机房Id查询机房名称 (GET)
     */
    @RequestMapping(value = "/online/queryRegionName", produces = "application/json")
    public Object queryRegionName(Integer regionId) {
        return gatewayService.loadLalancing(regionId);
    }

    /**
     * 订单优惠券抵扣 (POST json)
     */
    @RequestMapping(value = "/online/voucher/deduct", produces = "application/json")
    public Object deduct(@RequestBody VoucherDeductDTO param) {
        return gatewayService.loadLalancing(param);
    }

    /**
     * 基于Redis实现漏桶限流算法，并在API调用上体现
     */
    @RateLimit(key = "upstream",limitCount = 5,seconds = 5)
    @RequestMapping(value = "/online/listUpstreamInfo", produces = "application/json")
    public Object listUpstreamInfo() {
            return listUpstreamInfoService.listUpstreamInfo();
    }

}
