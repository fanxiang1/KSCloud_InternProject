package com.ksyun.start.camp.service;

import com.ksyun.start.camp.ApiResponse;
import com.ksyun.start.camp.dto.LogDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 日志服务的实现
 */
@Component
public class LoggingServiceImpl implements LoggingService {


    @Override
    public ApiResponse logging(LogDTO logDTO, Map<String, List<LogDTO>> logMap) {
        synchronized (this) {
            int count = 0;
            Iterator<Map.Entry<String, List<LogDTO>>> iterator = logMap.entrySet().iterator();
            ApiResponse apiResponse = new ApiResponse();
            while (iterator.hasNext()) {
                Map.Entry<String, List<LogDTO>> entry = iterator.next();
                List<LogDTO> instanceList = entry.getValue();
                Iterator<LogDTO> iteratorList = instanceList.iterator();
                while (iteratorList.hasNext()) {
                    iteratorList.next();
                    ++count;
                }
            }
            List<LogDTO> logList = logMap.get(logDTO.getServiceName());
            // 如果该服务未存过日志，则需要新建一个list进行日志存储
            if (logList == null) {
                logList = new ArrayList<>();
                logDTO.setLogId(++count);

                logList.add(logDTO);
                logMap.put(logDTO.getServiceName(), logList);
            } else {
                // 如果该服务名包含的没有此实例，将该实例添加进去
                if (!logList.contains(logDTO)) {
                    logDTO.setLogId(++count);
                    // 将该条日志添加到内存中
                    logList.add(logDTO);
                    logMap.put(logDTO.getServiceName(), logList);
                }
            }
            apiResponse.setCode(200);
            apiResponse.setData("日志记录成功");
            return apiResponse;
//            return "success";
        }
    }

    @Override
    public List<LogDTO> list(String serviceName, Map<String, List<LogDTO>> logMap) {
        // 将map中的元素全部按logId倒序排序
        List<LogDTO> logList = null;
        // 如果没有带参数，输出全部的数据
        if (StringUtils.isEmpty(serviceName)) {
            logList = logMap.entrySet().stream().flatMap(x -> x.getValue().stream()).collect(Collectors.toList());
            // 按logId倒序排序
            logList.sort(new Comparator<LogDTO>() {
                @Override
                public int compare(LogDTO o1, LogDTO o2) {
                    return o2.getLogId() - o1.getLogId();
                }
            });
        } else {
            try {
                logList = logMap.get(serviceName);
                //自定义排序，取出前五个
                logList.sort(new Comparator<LogDTO>() {
                    @Override
                    public int compare(LogDTO o1, LogDTO o2) {
                        return o2.getLogId() - o1.getLogId();
                    }
                });
                return logList.subList(0, 5);
            }catch (NullPointerException e){
                if (logList == null) {
                    logList = new ArrayList<>();
                }
                // throw new NullPointerException();
            }

        }
        return logList;
    }
}
