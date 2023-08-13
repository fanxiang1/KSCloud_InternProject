package com.ksyun.trade.config;

import com.ksyun.trade.interceptor.RateLimiterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimiterInterceptor rateLimiterInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 漏桶拦截器 添加拦截器并选择拦截路径
        registry.addInterceptor(rateLimiterInterceptor).addPathPatterns("/**");
    }
}
