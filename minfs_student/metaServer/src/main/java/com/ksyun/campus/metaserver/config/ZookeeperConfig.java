package com.ksyun.campus.metaserver.config;

import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "zookeeper")
@Data
public class ZookeeperConfig {

    /**
     * 集群地址
     */
    private String addr;

    /**
     * 连接超时时间
     */
    private Integer connectionTimeoutMs;
    /**
     * 会话超时时间
     */
    private Integer sessionTimeOut;

    /**
     * 重试机制时间参数
     */
    private Integer sleepMsBetweenRetry;

    /**
     * 重试机制重试次数
     */
    private Integer maxRetries;

    private String namespace;


    /**
     * curator 客户端
     * @return
     * @throws Exception
     */
    @Bean("curatorClient")
    public CuratorFramework curatorClient() throws Exception {
        // 开启客户端，并连接
        CuratorFramework client = CuratorFrameworkFactory.builder()
                //连接地址  集群用,隔开
                .connectString(addr)
                .connectionTimeoutMs(connectionTimeoutMs)
                //会话超时时间
                .sessionTimeoutMs(sessionTimeOut)
                //设置重试机制
                .retryPolicy(new ExponentialBackoffRetry(sleepMsBetweenRetry, maxRetries))
                //设置命名空间 在操作节点的时候，会以这个为父节点
                .namespace(namespace)
                .build();
        client.start();
        return client;
    }
}