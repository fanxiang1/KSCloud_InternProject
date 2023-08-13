package com.ksyun.trade.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ksyun.trade.dto.VoucherDeductDTO;
import com.ksyun.trade.rest.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import javax.servlet.http.HttpServletRequest;

@Service
public class GatewayService {

    @Autowired
    private LbService lbService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpServletRequest request;


    public Object loadLalancing(Object param) {
        // 1. 模拟路由 (负载均衡) 获取接口
        String name = lbService.random();


        // 2. 请求转发
        JSONObject userObject=new JSONObject();
        String method = request.getMethod();
        String uri = request.getRequestURI();//返回请求行中的资源名称
        //  要转发的地址
        String remote_url=name+uri;
        // 传参数
        if (request.getQueryString()!=null && !request.getQueryString().isEmpty()){
            remote_url += "/" + request.getQueryString();
        }
        //设置请求头参数
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("X-KSY-REQUEST-ID", request.getHeader("X-KSY-REQUEST-ID"));

        // 如果是post请求
        if(("POST").equalsIgnoreCase(method)){
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            // 序列化成json，在转成对象
            VoucherDeductDTO user = (VoucherDeductDTO) param;
            params.add("orderId", String.valueOf(user.getOrderId()));
            params.add("voucherNo",user.getVoucherNo());
            params.add("amount", String.valueOf(user.getAmount()));
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(params, requestHeaders);
            userObject = restTemplate.exchange(remote_url, HttpMethod.POST, httpEntity, JSONObject.class).getBody();
        }
        else{
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(requestHeaders);
            userObject = restTemplate.exchange(remote_url, HttpMethod.GET,httpEntity,JSONObject.class).getBody();
        }




        //System.out.println("返回的数据："+userObject);
        return userObject;
    }



}
