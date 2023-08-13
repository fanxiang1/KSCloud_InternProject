package com.ksyun.train.service.controller;

import com.alibaba.fastjson.JSONObject;
import com.ksyun.train.service.config.RestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;

@RestController
@RequestMapping
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    // 返回形如yyyy-MM-dd HH:mm:ss的json字符串数据，表示今天的日期
    @RequestMapping(value = "/api/echo",method = RequestMethod.GET, produces = "application/json")
    public Object echo() {
        String remote_url="http://service-provider/api/getDateTime";

        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(requestHeaders);
        Object time = restTemplate.exchange(remote_url, HttpMethod.GET,httpEntity, JSONObject.class).getBody();
        return time;
    }

}
