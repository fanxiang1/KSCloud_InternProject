package com.ksyun.trade.servers;

import java.util.HashMap;
import java.util.Map;

public class Servers {

    // 服务器列表，Key代表Ip，Value代表该Ip的权重
    public static Map<String, Integer> serverWeightMap = new HashMap();

    static {
        serverWeightMap.put("campus.query1.ksyun.com", 1);
        serverWeightMap.put("campus.query2.ksyun.com", 1);
        serverWeightMap.put("campus.query3.ksyun.com", 1);
        serverWeightMap.put("campus.query4.ksyun.com", 1);
        serverWeightMap.put("campus.query5.ksyun.com", 1);
    }
}