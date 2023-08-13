package com.ksyun.start.camp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ksyun.start.camp.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 服务启动运行逻辑
 */
@Component
@Slf4j
public class ServiceAppRunner implements ApplicationRunner {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spring.cloud.discovery.serverAddr}")
    public String serverAddress;

    @Value("${spring.application.name}")
    private String serviceName;

    public String serviceId = Constant.serviceId;

    private String ipAddress;

    @Value("${server.port}")
    private Integer port;

    private Boolean flag;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 此处代码会在 Boot 应用启动时执行

        // 开始编写你的逻辑，下面是提示
        // 1. 向 registry 服务注册当前服务
        // 2. 定期发送心跳逻辑

        // TODO
        register();
        beat();
    }

    // 向registry注册当前服务
    public Object register() throws Exception {
        InetAddress addr = InetAddress.getLocalHost();
        ipAddress = addr.getHostAddress();
        String remote_url="http://"+serverAddress + "/api/register";

        JSONObject jsonObject = new JSONObject();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/json");
        requestHeaders.add("charset", "utf-8");
        jsonObject.put("serviceName", serviceName);
        jsonObject.put("serviceId",serviceId);
        jsonObject.put("ipAddress", ipAddress);
        jsonObject.put("port", port);
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonObject.toString(), requestHeaders);
        try {
            JSONObject body = restTemplate.postForEntity(remote_url, httpEntity, JSONObject.class).getBody();
            log.info(body.toString());
            flag=true;
            return body;
        }catch (Exception e){
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setCode(500);
            apiResponse.setData("注册中心无法注册服务，请尽快检查");
            String s = JSON.toJSONString(apiResponse);
            JSONObject body = JSON.parseObject(s);
            flag=false;
            return body;
        }
    }

    @Scheduled(cron = "*/60 * * * * *")
    public Object beat() throws Exception{
        String remote_url="http://"+serverAddress+ "/api/heartbeat";

        JSONObject jsonObject = new JSONObject();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/json");
        requestHeaders.add("charset", "utf-8");
        jsonObject.put("serviceId",serviceId);
        jsonObject.put("ipAddress", ipAddress);
        jsonObject.put("port", port);
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonObject.toString(), requestHeaders);

//        JSONObject body = restTemplate.postForEntity(remote_url, httpEntity, JSONObject.class).getBody();
//        log.info(body.toString());
        if(flag){
            try {
                JSONObject body = restTemplate.postForEntity(remote_url, httpEntity, JSONObject.class).getBody();
                log.info(body.toString());
                return body;
            }catch (ResourceAccessException e){
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setCode(500);
                apiResponse.setData("注册中心无法接收心跳信息，请尽快检查");
                String s = JSON.toJSONString(apiResponse);
                JSONObject body = JSON.parseObject(s);
                log.info(body.toString());
                flag=false;
                return body;
            }
        }else {
            Object register = register();
            log.info(register.toString());
            return register;
        }
    }

    // 每一秒发送一次日志
    @Scheduled(cron = "*/1 * * * * *")
    public Object log() throws Exception{

//        String remote_url = "http://" + serverAddress + "/api/discovery?name=logging-service";
//        // 发送get请求到服务器
//        HttpHeaders requestHeaders = new HttpHeaders();
//        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(requestHeaders);
//        List list = restTemplate.exchange(remote_url, HttpMethod.GET, httpEntity, List.class).getBody();
//        Map map = (LinkedHashMap) list.get(0);

        InetAddress addr = InetAddress.getLocalHost();
        ipAddress = addr.getHostAddress();

        String service_url = "http://" + ipAddress + ":8320/api/logging";

        JSONObject jsonObject = new JSONObject();
        HttpHeaders request = new HttpHeaders();
        request.add("Content-Type", "application/json");
        request.add("charset", "utf-8");
        jsonObject.put("serviceName", serviceName);
        jsonObject.put("serviceId",serviceId);
        // 赋给时间
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String formattedDateTime = dateFormat.format(date);
        jsonObject.put("datetime", formattedDateTime);
        jsonObject.put("level", "info");
        jsonObject.put("message", "Client status is OK,time:"+formattedDateTime);
        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), request);
        JSONObject body = restTemplate.postForEntity(service_url, entity, JSONObject.class).getBody();
        log.info(body.toString());
        return body;
    }



    // 关闭时自动执行该方法
    @PreDestroy
    public Object destory() throws Exception{
        String remote_url="http://"+serverAddress+ "/api/unregister";

        JSONObject jsonObject = new JSONObject();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/json");
        requestHeaders.add("charset", "utf-8");
        jsonObject.put("serviceName", serviceName);
        jsonObject.put("serviceId",serviceId);
        jsonObject.put("ipAddress", ipAddress);
        jsonObject.put("port", port);
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonObject.toString(), requestHeaders);
        JSONObject body = restTemplate.postForEntity(remote_url, httpEntity, JSONObject.class).getBody();
        log.info(body.toString());
        return body;
    }
}
