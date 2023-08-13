package com.ksyun.campus.dataserver.services;


import com.ksyun.campus.dataserver.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RegistryService implements ApplicationRunner {

    @Autowired
    private CuratorFramework client;



    @Value("${application.name}")
    private String serviceName;

    private String host;
    {
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Value("${application.capacity}")
    private int capacity;

    @Value("${server.port}")
    private int port;

    @Value("${az.rack}")
    private String rack;

    @Value("${az.zone}")
    private String zone;

    private String path=null;

    private InetAddress address=null;




    @Override
    public void run(ApplicationArguments args) throws Exception {
        registryToCenter();
        createDataDir();
        updateData();
    }
    public void registryToCenter() throws Exception {
//        // todo 将本实例信息注册至zk中心，包含信息 ip、port、fileTotal、capacity、useCapacity、rack、zone、
        path="/data_"+port;
        File directory = new File("");
        String filePath = directory.getAbsolutePath()+path;
        File file = new File(filePath);
        // 获取一下数据的信息
        FileUtils fileUtils = new FileUtils();
        long useCapacity = fileUtils.getDirSize(file);
        int fileTotal = fileUtils.getDirCount(file);

        // 注册服务
        client.create().withMode(CreateMode.EPHEMERAL).forPath(path);
        // 写入数据
        String data1=host+","+port+","+String.valueOf(fileTotal)+","+capacity+","+String.valueOf(useCapacity)+","+rack+","+zone;
        client.setData().forPath(path,data1.getBytes());
    }

    /**
     * 定期更新节点数据
     * @throws Exception
     */
    @Scheduled(cron ="*/10 * * * * ?")
    public void updateData() throws Exception {
        path="/data_"+port;
        File directory = new File("");
        String filePath = directory.getAbsolutePath()+path;
        File file = new File(filePath);
        // 获取一下最新的数据信息
        FileUtils fileUtils = new FileUtils();
        long useCapacity = fileUtils.getDirSize(file);
        int fileTotal = fileUtils.getDirCount(file);
        // 写入数据
        String data1=host+","+port+","+String.valueOf(fileTotal)+","+capacity+","+String.valueOf(useCapacity)+","+rack+","+zone;
        client.setData().forPath(path,data1.getBytes());
    }


    public void createDataDir(){
        File directory = new File("");
        String filePath = directory.getAbsolutePath()+"/"+"data_"+port;
        File file = new File(filePath);
        // 创建目录
        if (!file.exists()) {
            file.mkdirs();// 目录不存在的情况下，创建目录。
        }
        System.out.println("创建数据根目录：" + filePath);
    }

    public List<Map<String, Integer>> getDslist() {
        return null;
    }


}
