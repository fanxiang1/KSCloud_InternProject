package com.ksyun.trade.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisCache implements com.ksyun.trade.cache.Cache{

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MemoryCache memoryCache;

    @Override
    public Object get(Object key) {
        Object o = redisTemplate.opsForValue().get(key);
        if(o==null){
            System.out.println("redis中没有数据，需要到数据库中查找");
            return "缓存中没有数据，需要到数据库中查找";
        }
        return "从redis中查到数据："+o;
    }

    @Override
    public void put(Object key, Object value) {
        redisTemplate.opsForValue().set(key,value);
        memoryCache.put(key,value);
        System.out.println("数据成功放入Redis中");
    }
}
