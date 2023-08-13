package com.ksyun.trade.service;

import com.ksyun.trade.dto.UpstreamDTO;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ListUpstreamInfoService {

    @Value("${actions:0}")
    public String ipAdd;
    public Object listUpstreamInfo() {
        UpstreamDTO upstreamDTO = new UpstreamDTO();
        upstreamDTO.setCode("200");
        upstreamDTO.setMsg("ok");
        upstreamDTO.setRequestId(MDC.get("traceId"));
        String[] split = ipAdd.split(",");
        upstreamDTO.setData(split);
        return upstreamDTO;
    }
}
