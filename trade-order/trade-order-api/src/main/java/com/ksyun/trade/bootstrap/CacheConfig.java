package com.ksyun.trade.bootstrap;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import com.ksyun.trade.cache.CacheKeyGenerator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 
 */
@Configuration
@EnableCaching(proxyTargetClass = true)
public class CacheConfig extends CachingConfigurerSupport {
    public static final String CACHEKEY_BASIC_COMMON = "basicCommon";

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new CacheKeyGenerator();
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        CaffeineCache baseDataCache = buildBaseDataCache(CACHEKEY_BASIC_COMMON);
        List<CaffeineCache> dataCacheList = Lists.newArrayList();
        dataCacheList.add(baseDataCache);
        simpleCacheManager.setCaches(dataCacheList);

        simpleCacheManager.afterPropertiesSet();

        CompositeCacheManager compositeCacheManager = new CompositeCacheManager(simpleCacheManager);
        //都找不到相应的cache时，不返回null，而是返回Spring内置的NOPCache
        compositeCacheManager.setFallbackToNoOpCache(true);
        return compositeCacheManager;
    }

    private CaffeineCache buildBaseDataCache(String name) {
        return new CaffeineCache(name, Caffeine.newBuilder()
            .expireAfterWrite(7200L, TimeUnit.SECONDS)
            .expireAfterAccess(7200L, TimeUnit.SECONDS)
            .initialCapacity(100)
            .maximumSize(1000L)
            .build());
    }

}
