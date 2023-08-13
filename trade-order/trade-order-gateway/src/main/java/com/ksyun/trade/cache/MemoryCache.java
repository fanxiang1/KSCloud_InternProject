package com.ksyun.trade.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class MemoryCache implements com.ksyun.trade.cache.Cache {

    @Autowired
    private RedisCache redisCache;
    /**
     * 本地缓存
     */
    private static Cache<Object, Object> localCache = CacheBuilder.newBuilder()
            .concurrencyLevel(16) // 并发级别
            .initialCapacity(1000) // 初始容量
            .maximumSize(10000) // 缓存最大长度
            .expireAfterAccess(1, TimeUnit.HOURS) // 缓存1小时没被使用就过期
            .build();


    /**
     * 获取缓存
     *
     * @param key
     * @return
     * @throws ExecutionException
     */
    @Override
    public Object get(Object key) throws ExecutionException {
        AtomicBoolean flag = new AtomicBoolean(false);
        //如果本地缓存没有数据，便从redis缓存中拉取数据
        Object ans = null;
        ans = localCache.get(key, () -> {
            flag.set(true);
            System.out.println("本地缓存未获取到数据，需要从redis缓存中获取");
            //                return redisTemplate.opsForValue().get(key);
            return redisCache.get(key);
        });
        if (flag.get() == false) {
            return "在本地缓存中查到数据" + ans;
        } else {
            return ans;
        }

    }

    /**
     * 放入缓存
     *
     * @param key
     * @param value
     */
    @Override
    public void put(Object key, Object value) {
        localCache.put(key, value);
        System.out.println("数据成功放入MemoryCache中");
    }
}
