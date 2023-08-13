package com.ksyun.start.camp.service;

import com.ksyun.start.camp.constant.Constant;
import com.ksyun.start.camp.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 客户端服务实现
 */
@Component
public class ClientServiceImpl implements ClientService {

    @Autowired
    private TimeService timeService;



    @Override
    public ResponseDTO getInfo() {

        // 开始编写你的逻辑，下面是提示
        // 1. 调用 TimeService 获取远端服务返回的时间
        // 2. 获取到自身的 serviceId 信息
        // 3. 组合相关信息返回

        String dateTime = timeService.getDateTime();
        ResponseDTO responseDTO = new ResponseDTO();
        String result="Hello Kingsoft Clound Star Camp - ["+Constant.serviceId+"] - "+dateTime;
        responseDTO.setError(null);
        responseDTO.setResult(result);

        return responseDTO;
    }
}
