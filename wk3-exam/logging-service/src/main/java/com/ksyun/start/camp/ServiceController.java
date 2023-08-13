package com.ksyun.start.camp;

import com.alibaba.fastjson.JSON;
import com.ksyun.start.camp.dto.LogDTO;
import com.ksyun.start.camp.service.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实现日志服务 API
 */
@RestController
@RequestMapping("/api")
public class ServiceController {

    // 保存信息
    private final Map<String, List<LogDTO>> logMap = new ConcurrentHashMap<>();

    @Autowired
    private LoggingService loggingService;

    // TODO: 实现日志服务 API
    @RequestMapping(value = "/logging", method = RequestMethod.POST,produces = "application/json")
    public Object logging(@RequestBody LogDTO logDTO) {
        return loggingService.logging(logDTO,logMap);
    }

    /**
     * 服务发现
     * @param serviceName
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET,produces = "application/json")
    public Object list(@RequestParam(value = "service",required = false) String serviceName) {
        List<LogDTO> list = loggingService.list(serviceName, logMap);
        // 转成json格式输出
        String s = JSON.toJSONString(list);
        List<LogDTO> jsonObject = JSON.parseArray(s, LogDTO.class);
        return jsonObject;
    }

}
