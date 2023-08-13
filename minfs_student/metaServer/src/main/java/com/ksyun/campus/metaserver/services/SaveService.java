package com.ksyun.campus.metaserver.services;

import com.alibaba.fastjson.JSON;
import com.ksyun.campus.metaserver.domain.StatInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceType;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;



/**
 * 保存元数据信息
 */
@Component
public class SaveService {
    @Autowired
    private CuratorFramework client;

//    @Value("${application.name}")
//    private String serviceName;

    private String path="/metadata";

    ServiceDiscovery<StatInfo> discovery =null;

    private String ip;


    @Value("${server.port}")
    private int port;

    public void metatoZk(StatInfo statInfo) throws Exception{
        // todo 将元信息注册至zk中心，包含信息:文件名、大小、创建、修改时间、内容数据的三副本索引
        InetAddress address = InetAddress.getLocalHost();
        ip = address.getHostAddress();

        // 2. 这个是用于序列化. 每一个服务节点的.
        InstanceSerializer<StatInfo> serializer = new InstanceSerializer<StatInfo>() {
            // 序列化过程 , 都是使用的json
            @Override
            public byte[] serialize(ServiceInstance<StatInfo> instance) throws Exception {
                StatInfo statInfo = instance.getPayload();
                return JSON.toJSONBytes(statInfo);
            }

            // 反序列化过程
            @Override
            public ServiceInstance<StatInfo> deserialize(byte[] bytes) throws Exception {
                StatInfo statInfo = JSON.parseObject(bytes, StatInfo.class);
                return ServiceInstance.<StatInfo>builder().payload(statInfo).name(path).id(statInfo.getPath()).address(ip).port(port).build();
            }
        };
        if(client.checkExists().forPath("/")==null){
            // 注册服务,永久节点
            client.create().withMode(CreateMode.PERSISTENT).forPath("/");
            // 写入数据
            newNode(path,statInfo,client,serializer);
        }else{
            // 写入数据
            newNode(path,statInfo,client,serializer);
        }
    }

    // 创建meta 节点
    public void newNode(String path, StatInfo statInfo ,CuratorFramework client, InstanceSerializer<StatInfo> serializer) throws Exception {

        // 1. 实例化一个 永久的服务节点
        ServiceInstance<StatInfo> instance = ServiceInstance.<StatInfo>builder().payload(statInfo).serviceType(ServiceType.PERMANENT).name(path).id(statInfo.getPath()).address(ip).port(port).build();

        // 2. 注册节点 . 此时需要执行start. 其实就是注册了该节点.
        discovery = ServiceDiscoveryBuilder.builder(StatInfo.class).basePath("/").client(client).serializer(serializer).thisInstance(instance).build();
        discovery.start();
    }


    // 得到节点数据
    public StatInfo getNode(String id,CuratorFramework client) throws Exception {
        // 2. 这个是用于序列化. 每一个服务节点的.
        InstanceSerializer<StatInfo> serializer = new InstanceSerializer<StatInfo>() {
            // 序列化过程 , 都是使用的json
            @Override
            public byte[] serialize(ServiceInstance<StatInfo> instance) throws Exception {
                StatInfo statInfo = instance.getPayload();
                return JSON.toJSONBytes(statInfo);
            }

            // 反序列化过程
            @Override
            public ServiceInstance<StatInfo> deserialize(byte[] bytes) throws Exception {
                StatInfo statInfo = JSON.parseObject(bytes, StatInfo.class);
                return ServiceInstance.<StatInfo>builder().payload(statInfo).name(path).id(statInfo.getPath()).address(ip).port(port).build();
            }
        };
        discovery = ServiceDiscoveryBuilder.builder(StatInfo.class).basePath("/").client(client).serializer(serializer).build();
        discovery.start();
        ServiceInstance<StatInfo> statInfoServiceInstance = discovery.queryForInstance(path, id);
        StatInfo statInfo = statInfoServiceInstance.getPayload();
        return statInfo;
    }


}
