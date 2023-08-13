package com.ksyun.campus.dataserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class DataService {

    @Value("${server.port}")
    private int port;
    @Autowired
    private RegistryService registryService;

    public void write(String path, byte[] data) throws Exception {
        File directory = new File("");
        boolean b = path.startsWith("/");//true
        if (!b) {
            path = "/" + path;
        }
        String filePath = directory.getAbsolutePath() + "/" + "data_" + port + path;
        File file = new File(filePath);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(data);
        outputStream.close();
        registryService.updateData();

        //todo 写本地
        //todo 调用远程ds服务写接口，同步副本，已达到多副本数量要求
        //todo 选择策略，按照 az rack->zone 的方式选取，将三副本均分到不同的az下
        //todo 支持重试机制
        //todo 返回三副本位置

    }

    public FileInputStream read(String path) {
        // 读取全部内容
        File directory = new File("");
        boolean b = path.startsWith("/");//true
        if (!b) {
            path = "/" + path;
        }
        String filePath = directory.getAbsolutePath() + "/" + "data_" + port + path;
        File file = new File(filePath);
        // 初始化输入流
        FileInputStream inputStream = null;
        try {
            // 创建字节输入流
            inputStream = new FileInputStream(file);
            return inputStream;
        } catch (FileNotFoundException e) {
            // 文件未找到时异常处理
            e.printStackTrace();
        } catch (IOException e) {
            // 读取过程中，删除文件会出此异常
            e.printStackTrace();
        }
        return null;
    }
}
