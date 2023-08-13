package com.ksyun.trade.config;

import com.ksyun.trade.interceptor.RateLimiterInterceptor;
import com.ksyun.trade.utils.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@ConditionalOnBean(RedisConnectionFactory.class)
@Slf4j
public class RateLimitConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
    @Bean
    @ConditionalOnMissingBean(RateLimiter.class)
    public RateLimiter rateLimiter() {
        return new RateLimiter();
    }
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean(RateLimiter.class)
    public RateLimiterInterceptor rateLimiterInterceptor() {
        return new RateLimiterInterceptor();
    }
}