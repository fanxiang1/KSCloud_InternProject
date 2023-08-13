package com.ksyun.start.camp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ksyun.start.camp.dto.RequestDTO;
import com.ksyun.start.camp.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class RegistryController {

    @Autowired
    private RegistryService registryService;


    // 保存信息
    private final Map<String, List<RequestDTO>> serviceMap = new ConcurrentHashMap<>();


    /**
     * 服务注册
     * @param requestDTO
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST,produces = "application/json")
    public Object register(@RequestBody RequestDTO requestDTO) {
        return registryService.register(requestDTO,serviceMap);
    }

    /**
     * 服务注销
     * @param requestDTO
     * @return
     */
    @RequestMapping(value = "/unregister", method = RequestMethod.POST,produces = "application/json")
    public Object unRegister(@RequestBody RequestDTO requestDTO) {
        return registryService.unRegister(requestDTO,serviceMap);
    }

    /**
     * 客户端发送心跳
     * @param requestDTO
     * @return
     */
    @RequestMapping(value = "/heartbeat", method = RequestMethod.POST,produces = "application/json")
    public Object heartBeat(@RequestBody RequestDTO requestDTO) {
        return registryService.hearBeat(requestDTO,serviceMap);
    }

    /**
     * 服务发现
     * @param serviceName
     * @return
     */
    @RequestMapping(value = "/discovery", method = RequestMethod.GET,produces = "application/json")
    public Object discovery(@RequestParam(value = "name",required = false) String serviceName) {
        List<RequestDTO> discovery = registryService.discovery(serviceName, serviceMap);
        // 转成json格式输出
        String s = JSON.toJSONString(discovery);
        List<RequestDTO> jsonObject = JSON.parseArray(s, RequestDTO.class);
        return jsonObject;
    }

    /**
     * 定时删除未传递的服务
     */
    @Scheduled(fixedDelay = 60000)
    public void clear(){
        // 移除serviceMap内的所有超60秒未发心跳的服务实例
        Iterator<Map.Entry<String, List<RequestDTO>>> iterator = serviceMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<RequestDTO>> entry = iterator.next();
            List<RequestDTO> instanceList = entry.getValue();
            // 移除心跳在60秒之前的实例
            instanceList.removeIf(x -> x.getBeatTime().before(new Date(new Date().getTime() - 61 * 1000)));
        }
    }




}
