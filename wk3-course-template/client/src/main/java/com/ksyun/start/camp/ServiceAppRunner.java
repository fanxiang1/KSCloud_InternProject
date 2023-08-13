package com.ksyun.start.camp;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 服务启动运行逻辑
 */
@Component
public class ServiceAppRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 此处代码会在 Boot 应用启动时执行

        // 开始编写你的逻辑，下面是提示
        // 1. 向 registry 服务注册当前服务
        // 2. 定期发送心跳逻辑

        // TODO
    }
}
