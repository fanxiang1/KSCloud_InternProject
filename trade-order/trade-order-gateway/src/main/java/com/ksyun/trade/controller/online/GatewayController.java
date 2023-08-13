package com.ksyun.trade.controller.online;

import com.ksyun.trade.service.GatewayService;
import com.ksyun.trade.service.LbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class GatewayController {
    @Autowired
    private GatewayService gatewayService;


    @Autowired
    private LbService lbService;

    @RequestMapping(value = "/online/query", produces = "application/json")
    public String query(Integer id) {
        String url = "http://"+lbService.random()+":8089/online/trade_order/2";
        RestTemplate restTemplate = new RestTemplate();
        String html = restTemplate.getForObject(url, String.class);
        return html;
//        return gatewayService.query(id);
    }

}
