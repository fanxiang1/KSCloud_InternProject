package com.ksyun.start.camp.service;

import com.alibaba.fastjson.JSONObject;
import com.ksyun.start.camp.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 代表远端时间服务接口实现
 */
@Component
public class TimeServiceImpl implements TimeService {

    @Value("${spring.cloud.discovery.serverAddr}")
    public String serverAddress;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getDateTime() {

        // 开始编写你的逻辑，下面是提示
        // 1. 连接到 registry 服务，获取远端服务列表
        // 2. 从远端服务列表中获取一个服务实例
        // 3. 执行远程调用，获取指定格式的时间
        try {
            String remote_url = "http://" + serverAddress + "/api/discovery?name=time-service";
            // 发送get请求到服务器
            HttpHeaders requestHeaders = new HttpHeaders();
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(requestHeaders);
            List list = restTemplate.exchange(remote_url, HttpMethod.GET, httpEntity, List.class).getBody();
            Map map = (LinkedHashMap) list.get(0);

            // 得到要调用的服务端连接
            String service_url = "http://" + map.get("ipAddress") + ":" + String.valueOf(map.get("port")) + "/api/getDateTime?style=full";
            // 发送get请求到服务器
            HttpHeaders request = new HttpHeaders();
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(request);
            JSONObject jsonObject = restTemplate.exchange(service_url, HttpMethod.GET, entity, JSONObject.class).getBody();
            String date = (String) jsonObject.get("result");

            // 格林威治时间转为北京时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = null;
            //将输入时间转换为ms
            sdf.parse(date).getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(sdf.parse(date).getTime());
//            System.out.println("格林威治时间：" + cal.getTime());
            cal.add(Calendar.HOUR, +8);
//            System.out.println("北京时间：" + cal.getTime());
            formattedDateTime = sdf.format(cal.getTime());
            return formattedDateTime;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
