package com.ksyun.trade.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class TwoLevelCache implements Cache{

    // 一级缓存
    @Autowired
    private MemoryCache memoryCache;
    // 二级缓存
    @Autowired
    private RedisCache redisCache;

    @Override
    public Object get(Object key) throws ExecutionException {
        // 先查一级缓存
        Object o = memoryCache.get(key);
        return o;
    }

    @Override
    public void put(Object key, Object value) {
        // 先放入redis缓存中数据
        redisCache.put(key,value);
    }
}
