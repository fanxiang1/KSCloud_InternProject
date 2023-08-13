package com.ksyun.trade.controller.online;

import com.ksyun.trade.service.DeductService;
import com.ksyun.trade.service.QueryOrderInfoService;
import com.ksyun.trade.service.QueryRegionNameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping
public class WorkerController {

    @Autowired
    private QueryOrderInfoService queryOrderInfoService;

    @Autowired
    private QueryRegionNameService queryRegionNameService;

    @Autowired
    private DeductService deductService;


    // 2、查询订单详情
    @RequestMapping(value = "/online/queryOrderInfo/id={id}", produces = "application/json")
    public Object queryOrderInfo(@PathVariable("id") String id) {
        Object o = queryOrderInfoService.queryOrderInfo(id);
        return o;
    }

    // 3、根据机房Id查询机房名称
    @RequestMapping(value = "/online/queryRegionName/regionId={id}", produces = "application/json")
    public Object queryRegionName(@PathVariable("id") String id) throws Exception {
        Object o = queryRegionNameService.queryRegionName(id);
        return o;
    }

    //4、订单优惠券抵扣公摊
    @RequestMapping(value = "/online/voucher/deduct", produces = "application/json")
    public Object deduct(@RequestParam("orderId") String orderId,
                                  @RequestParam("voucherNo") String voucherNo,
                                  @RequestParam("amount") String amount) {
        Object o = deductService.deduct(orderId,voucherNo,amount);
        return o;
    }
}
