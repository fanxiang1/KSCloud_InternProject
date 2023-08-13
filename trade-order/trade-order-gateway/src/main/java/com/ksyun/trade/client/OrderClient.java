package com.ksyun.trade.client;

import com.ksyun.trade.bo.InnerHttpResp;
import com.ksyun.trade.rest.RestConsts;
import com.ksyun.trade.util.HttpToolKits;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class OrderClient {

    public String query(String url) {
        try {
            InnerHttpResp httpResp = HttpToolKits.get(url, new HashMap<>(), new HashMap<>());
            if (httpResp.getStatusCode() != RestConsts.DEFAULT_SUCCESS_CODE) {
                throw new RuntimeException(httpResp.getExceptionMsg());
            }
            return httpResp.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
