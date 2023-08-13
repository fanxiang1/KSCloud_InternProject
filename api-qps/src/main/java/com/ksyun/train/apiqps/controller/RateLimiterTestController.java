package com.ksyun.train.apiqps.controller;

import com.ksyun.train.apiqps.annotation.RateLimiterAnnotation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class RateLimiterTestController {
    @RateLimiterAnnotation()
    @RequestMapping("/test/instance/query")
    public String describeInstance() {

        String config_path = System.getProperty("config_path");
        System.out.println(config_path);
        return "describe instance success";
    }


    @RateLimiterAnnotation
    @RequestMapping("/test/instance/create")
    public String createInstance() {
        return "create instance success";
    }
}
