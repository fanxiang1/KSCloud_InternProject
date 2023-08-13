package com.ksyun.start.camp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 代表微服务客户端
 */
@SpringBootApplication
@EnableScheduling
public class ClientApp {

    public static void main(String[] args) {
        SpringApplication.run(ClientApp.class, args);
    }
}
