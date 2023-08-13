package com.ksyun.trade.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegionClient {
    @Value("${meta.url}")
    private String url;



}
