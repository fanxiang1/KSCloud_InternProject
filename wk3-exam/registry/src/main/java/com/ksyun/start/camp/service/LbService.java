package com.ksyun.start.camp.service;

import com.ksyun.start.camp.dto.RequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LbService {

    private Integer pos = 0;

    /**
     * 轮询算法
     * @return
     */
    public RequestDTO round(List<RequestDTO> list) {
        // 重建一个Map，避免服务器的上下线导致的并发问题
        Map<RequestDTO, Integer> serverMap = new HashMap(16);
        // 如果存在该服务，需要去注销完全符合的那个实例
        Iterator<RequestDTO> iterator = list.iterator();
        // 放到map里面
        while (iterator.hasNext()){
            RequestDTO temp = iterator.next();
            serverMap.put(temp,1);
        }

        //  取得Server地址List
        Set<RequestDTO> keySet = serverMap.keySet();
        List<RequestDTO> keyList = new ArrayList();
        keyList.addAll(keySet);

        RequestDTO server = null;
        // 更好的实现使用cas ,这里简单演示, 先使用锁 ，Atomic也可以
        synchronized (pos) {
            if (pos >= keySet.size()) {
                pos = 0;
            }
            server = keyList.get(pos);
            pos++;
        }

        return server;
    }
}
