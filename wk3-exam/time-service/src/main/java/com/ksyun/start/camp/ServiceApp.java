package com.ksyun.start.camp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 基于 Spring Boot 实现的微服务
 * 时间服务
 */
@SpringBootApplication
@EnableScheduling
public class ServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApp.class, args);
    }
}
