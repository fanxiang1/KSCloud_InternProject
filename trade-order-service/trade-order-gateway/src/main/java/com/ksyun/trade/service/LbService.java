package com.ksyun.trade.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class LbService {

    private Integer pos = 0;

    public static final String UNKNOWN = "unknown";

    @Value("${actions:0}")
    public String ipAdd;

    /**
     * 随机算法
     * @return
     */
    public String random(){
        // 重建一个Map，避免服务器的上下线导致的并发问题
        Map<String, Integer> serverMap = new HashMap(16);
        String[] split = ipAdd.split(",");
        for (String s:split){
            serverMap.put(s,1);
        }

        //  取得Server地址List
        Set<String> keySet = serverMap.keySet();
        ArrayList<String> keyList = new ArrayList<String>();
        keyList.addAll(keySet);

        Random random = new Random();
        int randomPos = random.nextInt(keyList.size());

        return keyList.get(randomPos);
    }

    /**
     * 轮询算法
     * @return
     */
    public String round() {
        // 重建一个Map，避免服务器的上下线导致的并发问题
        Map<String, Integer> serverMap = new HashMap(16);
        String[] split = ipAdd.split(",");
        for (String s:split){
            serverMap.put(s,1);
        }

        //  取得Server地址List
        Set<String> keySet = serverMap.keySet();
        List<String> keyList = new ArrayList();
        keyList.addAll(keySet);

        String server = null;
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

    /**
     * hash算法
     * @return
     */
//    public String hash(HttpServletRequest request){
//        // 重建一个Map，避免服务器的上下线导致的并发问题
//        Map<String, Integer> serverMap = new HashMap(16);
//        serverMap.putAll(Servers.serverWeightMap);
//
//        // 取得Server地址List
//        Set<String> keySet = serverMap.keySet();
//        ArrayList<String> keyList = new ArrayList<String>();
//        keyList.addAll(keySet);
//
//        // 在Web应用中可通过HttpServlet的getRemoteIp方法获取
//        String remoteIp = this.getIpAddr(request);
//        int hashCode = remoteIp.hashCode();
//        int serverListSize = keyList.size();
//        int serverPos = hashCode % serverListSize;
//
//        return keyList.get(serverPos);
//    }


    /**
     * 权重算法
     * @return
     */
//    public String weight(){
//        // 重建一个Map，避免服务器的上下线导致的并发问题
//        Map<String, Integer> serverMap = new HashMap(16);
//        serverMap.putAll(Servers.serverWeightMap);
//
//        // 取得Server地址List
//        Set<String> keySet = serverMap.keySet();
//        Iterator<String> iterator = keySet.iterator();
//
//        List<String> serverList = new ArrayList<String>();
//        while (iterator.hasNext()) {
//            String server = iterator.next();
//            int weight = serverMap.get(server);
//            for (int i = 0; i < weight; i++) {
//                serverList.add(server);
//            }
//        }
//
//        String server = null;
//        synchronized (pos) {
//            if (pos >= keySet.size()) {
//                pos = 0;
//            }
//            server = serverList.get(pos);
//            pos++;
//        }
//
//        return server;
//    }
//
//    // 获取请求过来的IP
//    public String getIpAddr(HttpServletRequest request) {
//        if (request == null) {
//            return "";
//        }
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
//            ip = request.getHeader("X-Forwarded-For");
//        }
//        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
//            ip = request.getHeader("X-Real-IP");
//        }
//
//        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        return ip;
//    }


}
