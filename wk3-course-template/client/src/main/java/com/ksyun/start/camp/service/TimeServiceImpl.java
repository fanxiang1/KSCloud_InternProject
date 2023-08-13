package com.ksyun.start.camp.service;

import com.ksyun.start.camp.service.TimeService;
import org.springframework.stereotype.Component;

/**
 * 代表远端时间服务接口实现
 */
@Component
public class TimeServiceImpl implements TimeService {

    @Override
    public String getDateTime(String style) {

        // 开始编写你的逻辑，下面是提示
        // 1. 连接到 registry 服务，获取远端服务列表
        // 2. 从远端服务列表中获取一个服务实例
        // 3. 执行远程调用，获取指定格式的时间

        return null;
    }
}
