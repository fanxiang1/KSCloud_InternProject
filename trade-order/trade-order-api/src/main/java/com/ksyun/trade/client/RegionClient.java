package com.ksyun.trade.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ksyun.common.util.mapper.JacksonMapper;
import com.ksyun.trade.bo.InnerHttpResp;
import com.ksyun.trade.dao.dataobject.Region;
import com.ksyun.trade.exception.BusinessException;
import com.ksyun.trade.rest.RestResult;
import com.ksyun.trade.util.HttpToolKits;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RegionClient {
    @Value("${basic.url}")
    private String url;
    private final static String URL_REGION_LIST = "/online/region/list";

    public List<Region> queryAll() {

        return null;
    }


}
