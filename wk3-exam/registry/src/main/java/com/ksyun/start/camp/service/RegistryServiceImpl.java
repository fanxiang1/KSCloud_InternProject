package com.ksyun.start.camp.service;

import com.alibaba.fastjson.JSONObject;
import com.ksyun.start.camp.dto.ApiResponse;
import com.ksyun.start.camp.dto.RequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sun.util.calendar.BaseCalendar;

import javax.annotation.PreDestroy;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RegistryServiceImpl implements RegistryService {

    @Autowired
    private LbService lbService;


    // 服务注册
    @Override
    public ApiResponse register(RequestDTO requestDTO, Map<String, List<RequestDTO>> serviceMap) {
        synchronized (this) {
            List<RequestDTO> instanceList = serviceMap.get(requestDTO.getServiceName());
            ApiResponse apiResponse = new ApiResponse();
            // 如果该服务未注册，需要进行注册
            if (instanceList == null) {
                instanceList = new ArrayList<>();
                requestDTO.setBeatTime(new Date()); // 加入时间
                instanceList.add(requestDTO);
                serviceMap.put(requestDTO.getServiceName(), instanceList);
            } else {
                boolean flag=false;
                // 根据实例id更新数据
                Iterator<RequestDTO> iterator = instanceList.iterator();
                while (iterator.hasNext()) {
                    RequestDTO temp = iterator.next();
                    if(temp.getServiceId().equals(requestDTO.getServiceId())){
                        flag=true;
                        temp.setServiceName(requestDTO.getServiceName());
                        temp.setPort(requestDTO.getPort());
                        temp.setIpAddress(requestDTO.getIpAddress());
                        temp.setBeatTime(new Date()); // 加入时间
                        break;
                    }
                }
                if(!flag){
                    // 如果该服务名包含的没有此实例，将该实例添加进去
                    if (!instanceList.contains(requestDTO)) {
                        requestDTO.setBeatTime(new Date()); // 加入时间
                        // 将该实例添加到服务名中
                        instanceList.add(requestDTO);
                        serviceMap.put(requestDTO.getServiceName(), instanceList);
                    } else {
                        // 该实例已经注册过了
                        apiResponse.setCode(500);
                        apiResponse.setData("不可重复注册同一服务");
                        return apiResponse;
//                    return "不可重复注册同一服务";
                    }
                }else {
                    apiResponse.setCode(200);
                    apiResponse.setData("更新服务信息");
                    return apiResponse;
                }

            }
            log.info("========注册成功====== ");
            log.info("服务名： " + requestDTO.getServiceName());
            log.info("实例id： " + requestDTO.getServiceId());
            log.info("ip地址： " + requestDTO.getIpAddress());
            log.info("端口： " + requestDTO.getPort());
            log.info("心跳时间：" + requestDTO.getBeatTime());
            apiResponse.setCode(200);
            apiResponse.setData("服务注册成功");
            return apiResponse;
        }
//        return "服务注册成功";
    }


    // 客户端发送心跳,加锁执行
    @Override
    public ApiResponse hearBeat(RequestDTO requestDTO, Map<String, List<RequestDTO>> serviceMap) {
        synchronized (this) {
            Iterator<Map.Entry<String, List<RequestDTO>>> iterator = serviceMap.entrySet().iterator();
            ApiResponse apiResponse = new ApiResponse();
            while (iterator.hasNext()) {
                Map.Entry<String, List<RequestDTO>> entry = iterator.next();
                List<RequestDTO> instanceList = entry.getValue();
                // 判断传过来的serviceId在这个list中
                if (instanceList.contains(requestDTO)) {
                    RequestDTO instance = instanceList.stream().filter(x -> x.getServiceId().equals(requestDTO.getServiceId()) && x.getIpAddress().equals(requestDTO.getIpAddress()) && x.getPort().equals(requestDTO.getPort())).findFirst().orElse(null);
                    instance.setBeatTime(new Date());
                    log.info("========心跳检测更新成功=======");
                    log.info("服务名： " + instance.getServiceName());
                    log.info("实例id： " + instance.getServiceId());
                    log.info("ip地址： " + instance.getIpAddress());
                    log.info("端口： " + instance.getPort());
                    log.info("心跳时间：" + instance.getBeatTime());
                    apiResponse.setCode(200);
                    apiResponse.setData("心跳发送成功");
                    return apiResponse;
                }
                // 如果移除完了list为空，则直接去除这个键值对
                if (instanceList.size() == 0) {
                    iterator.remove();
                }
            }
            apiResponse.setCode(500);
            apiResponse.setData("未检测到对应到服务");
            return apiResponse;
        }
    }

    // 服务注销
    @Override
    public ApiResponse unRegister(RequestDTO requestDTO, Map<String, List<RequestDTO>> serviceMap) {
        List<RequestDTO> instanceList = serviceMap.get(requestDTO.getServiceName());
        ApiResponse apiResponse = new ApiResponse();
        // 如果不存在该服务
        if (CollectionUtils.isEmpty(instanceList)) {
            apiResponse.setCode(500);
            apiResponse.setData("该服务不在注册中心中,请检查");
            return apiResponse;
//            return "该服务不在注册中心中,请检查";
        }
        // 如果存在该服务，需要去注销完全符合的那个实例
        Iterator<RequestDTO> iterator = instanceList.iterator();
        boolean flag = false;
        while (iterator.hasNext()) {
            RequestDTO temp = iterator.next();
            // 要删除的实例
            if (temp.equals(requestDTO)) {
                iterator.remove();
                flag = true;
                log.info("========心跳检测更新成功=======");
                log.info("服务名： " + temp.getServiceName());
                log.info("实例id： " + temp.getServiceId());
                log.info("ip地址： " + temp.getIpAddress());
                log.info("端口： " + temp.getPort());
                log.info("心跳时间：" + temp.getBeatTime());
            }
        }
        if (flag) {
            apiResponse.setCode(200);
            apiResponse.setData("服务注销成功");
        } else {
            apiResponse.setCode(500);
            apiResponse.setData("该服务不在注册中心中,请检查");
        }
        return apiResponse;
//        return "服务注销成功";
    }

    // 服务发现
    @Override
    public List<RequestDTO> discovery(String serviceName, Map<String, List<RequestDTO>> serviceMap) {
        List<RequestDTO> instanceList = null;
        // 如果没有带参数，输出全部的数据
        if (StringUtils.isEmpty(serviceName)) {
            instanceList = serviceMap.entrySet().stream().flatMap(x -> x.getValue().stream()).collect(Collectors.toList());
        } else {
            instanceList = serviceMap.get(serviceName);
            if(instanceList==null){
                instanceList = new ArrayList<>();
                return instanceList;
            }
            // 使用轮询随机输出一个
            RequestDTO round = lbService.round(instanceList);
            List<RequestDTO> lbInstance = new ArrayList<RequestDTO>();
            lbInstance.add(round);
            return lbInstance;
        }
        // 移除心跳在60秒之前的实例
        instanceList.removeIf(x -> x.getBeatTime().before(new Date(new Date().getTime() - 60 * 1000)));

        return instanceList;
    }


}
