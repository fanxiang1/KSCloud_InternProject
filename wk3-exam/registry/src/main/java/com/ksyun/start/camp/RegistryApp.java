package com.ksyun.start.camp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 微服务注册中心
 */
@SpringBootApplication
@EnableScheduling
public class RegistryApp {

    public static void main(String[] args) {
        SpringApplication.run(RegistryApp.class, args);
    }
}
