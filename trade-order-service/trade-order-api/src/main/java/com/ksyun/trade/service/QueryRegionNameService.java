package com.ksyun.trade.service;


import com.alibaba.fastjson.JSONObject;
import com.ksyun.trade.dto.RegionNameDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class QueryRegionNameService {

    @Value("${meta.url:0}")
    public String ipAdd;

    @Autowired
    private RestTemplate restTemplate;

    // 调用远程接口，并返回json数据
    @Retryable(value = Exception.class, maxAttempts = 10, backoff = @Backoff(delay = 2000,multiplier = 1.5))
    public Object queryRegionName(String id) throws Exception {
        String requestId= MDC.get("traceId");
        Integer regionId = Integer.valueOf(id);

        String url = ipAdd + "/online/region/name/" + Integer.valueOf(regionId);
        JSONObject regionJson = restTemplate.getForObject(url, JSONObject.class);
        Integer code = (Integer) regionJson.get("code");
        if(!code.equals(200)) {
            // 这里可以使自定义异常，@Retryable中value需与其一致
            throw new Exception("接口调用失败");
        }
        Object msg = regionJson.get("msg");
        Object data = regionJson.get("data");

        RegionNameDTO regionNameDTO = new RegionNameDTO();
        regionNameDTO.setCode(code);
        regionNameDTO.setMsg((String)msg);
        regionNameDTO.setRequestId(requestId);
        regionNameDTO.setData((String)data);

        Object o = JSONObject.toJSON(regionNameDTO);
        return o;
    }
}
