package com.ksyun.train.apiqps.config;

import com.ksyun.train.apiqps.interceptor.LimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IntercepterConfig  implements WebMvcConfigurer {
    @Autowired
    private LimitInterceptor limitInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(limitInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**");
    }
}