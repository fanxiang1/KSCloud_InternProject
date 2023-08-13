package com.ksyun.campus.client.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import javax.annotation.PostConstruct;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ZkUtil {

    // 开启客户端连接
    private CuratorFramework client = CuratorFrameworkFactory.builder()
            //连接地址  集群用,隔开
            .connectString("127.0.0.1:2181")
            .retryPolicy(new ExponentialBackoffRetry(50, 100))
            .build();

    public static final String SERVICES_BASE_PATH = "/meta/services";

    private static CountDownLatch watch = new CountDownLatch(1);

    // 得到metaServer的ip+port
    public static String metaPath = null;


    /**
     * 注册监听
     * TreeCache: 可以将指定的路径节点作为根节点（祖先节点），对其所有的子节点操作进行监听，
     * 呈现树形目录的监听，可以设置监听深度，最大监听深度为 int 类型的最大值。
     */

    public void zkWatch(CuratorFramework client,String path) throws Exception {
        TreeCache treeCache = new TreeCache(client, path);

        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                ChildData eventData = event.getData();
                String s=null;
                String[] split;
                switch (event.getType()) {
                    case NODE_ADDED:
                        s = new String(client.getData().forPath(path + "/master"));
                        split = s.split(",");
                        metaPath=split[0]+":"+split[1];
                        log.warn(path + "节点添加" + eventData.getPath() + "\t添加数据为：" + new String(eventData.getData()));
                        break;
                    case NODE_UPDATED:
                        s = new String(client.getData().forPath(path + "/master"));
                        split = s.split(",");
                        metaPath=split[0]+":"+split[1];
                        log.warn(eventData.getPath() + "节点数据更新\t更新数据为：" + new String(eventData.getData()) + "\t版本为：" + eventData.getStat().getVersion());
                        break;
                    case NODE_REMOVED:
                        s = new String(client.getData().forPath(path + "/master"));
                        split = s.split(",");
                        metaPath=split[0]+":"+split[1];
                        log.warn(eventData.getPath() + "节点被删除");
                        break;
                    default:
                        break;
                }
            }
        });
        treeCache.start();
        watch.await();  //如果不执行 watch.countDown()，进程会一致阻塞在 watch.await()
    }


    @PostConstruct
    public void postCons() throws Exception {
        // todo 初始化，与zk建立连接，注册监听路径，当配置有变化随时更新
        // todo 通过zk内注册的master，得到对应的ip+port，用来和metaServer进行交互
        client.start();
        String s = new String(client.getData().forPath(SERVICES_BASE_PATH + "/master"));
        String[] split = s.split(",");
        metaPath=split[0]+":"+split[1];
        System.out.println(metaPath);
        //zkWatch(client,SERVICES_BASE_PATH);
    }
}
