package com.ksyun.trade.controller.online;


import com.ksyun.trade.service.LbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@RestController
public class LbController {

    @Autowired
    private LbService lbService;

    /**
     * 随机访问
     * @return
     */
    @RequestMapping(value = "/online/random", method = {RequestMethod.GET})
    public String random() {
        String random = lbService.random();
        return random;
    }

    /**
     * hash访问
     * @return
     */
    @RequestMapping(value = "/online/hash", method = {RequestMethod.GET})
    public String hash(HttpServletRequest request) {
        String hash = lbService.hash(request);
        return hash;
    }

    /**
     * 轮询访问
     * @return
     */
    @RequestMapping(value = "/online/round", method = {RequestMethod.GET})
    public String round() {
        String s = lbService.round();
        return s;
    }

    /**
     * 权重访问
     * @return
     */
    @RequestMapping(value = "/online/weight", method = {RequestMethod.GET})
    public String weight() {
        String weight = lbService.weight();
        return weight;
    }
}
