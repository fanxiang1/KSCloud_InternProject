package com.ksyun.campus.metaserver.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RegistService implements ApplicationRunner {
    @Autowired
    private CuratorFramework client;

    @Value("${application.name}")
    private String serviceName;

    private String ip;

    private String path = null;

    private LeaderLatch latch;

    //全局变量标识-当前节点是否为master
    private boolean isMaster = false;

    @Value("${server.port}")
    private int port;

    String masterNodePath = "/services/master";

    String slaveNodePath = "/services/slave";

    public volatile boolean master = false;

    private static final Long WAIT_SECONDS = 3L;

    public void registToCenter() throws Exception {
        // todo 将本实例信息注册至zk中心，包含信息 ip、port
        regist();
        PathChildrenCache childrenCache = new PathChildrenCache(client, "/services", true);
        childrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        // 节点数据change事件的通知方法


        childrenCache.getListenable().addListener((curatorFramework, event) -> {
            if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                String childPath = event.getData().getPath();
                System.out.println("child removed: " + childPath);
                System.out.println("节点变更，开始重新选举");
                //todo：防止脑裂/防止网络抖动/数据同步
                try {
                    TimeUnit.SECONDS.sleep(WAIT_SECONDS);
                } catch (InterruptedException ignored) {
                }
                // 删除slave节点并重新创建master节点
                change();
            }
        });
    }

    private void regist() throws Exception {
        InetAddress address = InetAddress.getLocalHost();
        String hostAddress = address.getHostAddress();
        // 写入数据
        String data = hostAddress + "," + port;
        System.out.printf("机器【%s】开始抢占 Master", port);
        System.out.println();
        try {
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(masterNodePath);
            client.setData().forPath(masterNodePath,data.getBytes());
            System.out.printf("机器【%s】成为了 Master", port);
            System.out.println();
            master = true;
        } catch (Exception e) {
            System.out.printf("机器【%s】抢占 Master 失败", port);
            // 变成slave节点
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(slaveNodePath);
            client.setData().forPath(slaveNodePath,data.getBytes());
            System.out.println();
            master = false;
        }
    }

    private void change() throws Exception {
        InetAddress address = InetAddress.getLocalHost();
        String hostAddress = address.getHostAddress();
        // 写入数据
        String data = hostAddress + "," + port;
        System.out.printf("机器【%s】开始切换为Master", port);
        System.out.println();
        try {
            // 删除slave节点
            client.delete()
                    .deletingChildrenIfNeeded()
                    .forPath(slaveNodePath);
            // 新建master节点
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(masterNodePath);
            client.setData().forPath(masterNodePath,data.getBytes());
            System.out.printf("机器【%s】成为了 Master", port);
            System.out.println();
            master = true;
        } catch (Exception e) {
            System.out.printf("机器【%s】切换 Master 失败", port);
            System.out.println();
            master = false;
        }
    }

    public void stop() {
        CloseableUtils.closeQuietly(latch);
        CloseableUtils.closeQuietly(client);
    }

    public boolean getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(boolean isMaster) {
        this.isMaster = isMaster;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 注册元数据要存储的根节点
//        client.create().forPath("/metadata");
        registToCenter();
    }
}
