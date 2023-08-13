package com.ksyun.start.camp.service;

import com.ksyun.start.camp.ApiResponse;
import com.ksyun.start.camp.dto.LogDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 日志服务实现接口
 */
public interface LoggingService {

    // TODO: 实现日志服务接口
    // 此处不再重复提示骨架代码，可参考其他 Service 接口的定义
    public ApiResponse logging(LogDTO logDTO, Map<String, List<LogDTO>> logMap);

    public List<LogDTO> list(String serviceName, Map<String, List<LogDTO>> logMap);

}
