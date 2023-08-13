package com.ksyun.trade.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ksyun.trade.dto.*;
import com.ksyun.trade.entity.KscTradeOrder;
import com.ksyun.trade.entity.KscTradeProductConfig;
import com.ksyun.trade.entity.Region;
import com.ksyun.trade.entity.User;
import com.ksyun.trade.utils.RedisUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class QueryOrderInfoService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private KscTradeOrderService kscTradeOrderService;

    @Autowired
    private KscTradeProductConfigService kscTradeProductConfigService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Value("${meta.url:0}")
    public String ipAdd;

    // 调用远程接口，并返回json数据
    public Object queryOrderInfo(String id)  {
        // 程序开始时间
        long startTime = System.currentTimeMillis();
        // 先去redis中查对应的数据
        if (redisUtils.hasKey("order_" + id)) {
            JSONObject o = (JSONObject) redisUtils.get("order_" + id);
            // 程序结束时间
            long endTime = System.currentTimeMillis();
            String descr = (double) (endTime - startTime) / 1000 + "秒";
            o.put("descr", descr);
            return o;
        }
        // 远端调用接口
        String upsteam = request.getServerName();
        String requestId = MDC.get("traceId");
        Integer true_Id = Integer.valueOf(id);
        // 根据id查出对应的user_id和gigion_id
        LambdaQueryWrapper<KscTradeOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KscTradeOrder::getId, true_Id);
        KscTradeOrder tradeOrder = kscTradeOrderService.getOne(queryWrapper);
        Integer userId = tradeOrder.getUserId();
        Integer regionId = tradeOrder.getRegionId();
        BigDecimal priceValue = tradeOrder.getPriceValue();

        // 新建DTO对象
        UserDTO userDTO = new UserDTO();
        RegionDTO regionDTO = new RegionDTO();
        List<ConfigsDTO> configsDTOList = new ArrayList<>();
        TradeOrderDataDTO tradeOrderDataDTO = new TradeOrderDataDTO();
        TradeOrderDTO tradeOrderDTO = new TradeOrderDTO();

        //开启线程个数
        int threadCount = 2;
        //所有线程阻塞，然后统一开始
        CountDownLatch begin = new CountDownLatch(1);
        //主线程阻塞，直到所有分线程执行完毕
        CountDownLatch end = new CountDownLatch(threadCount);
        //开始多线程
        begin.countDown();
        for (Integer i = 0; i < threadCount; i++) {
            if (i == 0){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 得到用户信息数据
                        // http://campus.meta.ksyun.com:8090/online/user/{id}
                        String url1 = ipAdd + "/online/user/" + Integer.valueOf(userId);
                        JSONObject userJson = restTemplate.getForObject(url1, JSONObject.class);
                        JSONObject userData = userJson.getJSONObject("data");
                        User user = JSONObject.toJavaObject(userData, User.class);
                        // 得到config数据
                        // 查询对应的参数设置
                        LambdaQueryWrapper<KscTradeProductConfig> queryWrapperConfig = new LambdaQueryWrapper<>();
                        queryWrapperConfig.eq(KscTradeProductConfig::getOrderId, id);
                        //SQL:select * from ksc_trade_product_config where order_id=?
                        List<KscTradeProductConfig> list = kscTradeProductConfigService.list(queryWrapperConfig);
                        //System.out.println(list);

                        // userDTO
                        userDTO.setUserName(user.getUserName());
                        userDTO.setEmail(user.getEmail());
                        userDTO.setPhone(user.getPhone());
                        userDTO.setAddress(user.getAddress());
                        // configsDTO,可能是一组数据
                        ConfigsDTO configsDTO = new ConfigsDTO();
                        for (KscTradeProductConfig config : list) {
                            configsDTO.setItemNo(config.getItemNo());
                            configsDTO.setItemName(config.getItemName());
                            configsDTO.setUnit(config.getUnit());
                            configsDTO.setValue(config.getValue());
                            configsDTOList.add(configsDTO);
                        }
                        end.countDown();
                    }
                }).start();
            }
            if(i==1){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 查看redis中是否有对应的地区信息，没有的话去接口得到地区信息数据并存入redis中
                        Region region = null;
                        if (redisUtils.hasKey("region_" + regionId)) {
                            JSONObject o = (JSONObject) redisUtils.get("region_" + regionId);
                            region = JSONObject.toJavaObject(o, Region.class);
                        } else {
                            // http://campus.meta.ksyun.com:8090/online/region/list
                            String url2 = ipAdd + "online/region/list";
                            JSONObject regions = restTemplate.getForObject(url2, JSONObject.class);
                            JSONArray array = regions.getJSONArray("data");
                            // 将数据全部放到redis中
                            for (int i = 0; i < array.size(); i++) {
                                JSONObject regionData = array.getJSONObject(i);
                                region = JSONObject.toJavaObject(regionData, Region.class);
                                Object o = JSONObject.toJSON(region);
                                redisUtils.set("region_" + region.getId(), o);
                            }
                            for (int i = 0; i < array.size(); i++) {
                                if (regionId.equals(array.getJSONObject(i).get("id"))) {
                                    JSONObject regionData = array.getJSONObject(i);
                                    region = JSONObject.toJavaObject(regionData, Region.class);
                                    break;
                                }
                            }
                        }
                        regionDTO.setCode(region.getCode());
                        regionDTO.setName(region.getName());
                        end.countDown();
                    }
                }).start();
            }
        }


        // 得到用户信息数据
        // http://campus.meta.ksyun.com:8090/online/user/{id}
//        String url1 = ipAdd + "/online/user/" + Integer.valueOf(userId);
//        JSONObject userJson = restTemplate.getForObject(url1, JSONObject.class);
//        JSONObject userData = userJson.getJSONObject("data");
//        User user = JSONObject.toJavaObject(userData, User.class);

        // 查看redis中是否有对应的地区信息，没有的话去接口得到地区信息数据并存入redis中
//        Region region = null;
//        if (redisUtils.hasKey("region_" + regionId)) {
//            JSONObject o = (JSONObject) redisUtils.get("region_" + regionId);
//            region = JSONObject.toJavaObject(o, Region.class);
//        } else {
//            // http://campus.meta.ksyun.com:8090/online/region/list
//            String url2 = ipAdd + "online/region/list";
//            JSONObject regions = restTemplate.getForObject(url2, JSONObject.class);
//            JSONArray array = regions.getJSONArray("data");
//            // 将数据全部放到redis中
//            for (int i = 0; i < array.size(); i++) {
//                JSONObject regionData = array.getJSONObject(i);
//                region = JSONObject.toJavaObject(regionData, Region.class);
//                Object o = JSONObject.toJSON(region);
//                redisUtils.set("region_" + region.getId(), o);
//            }
//            for (int i = 0; i < array.size(); i++) {
//                if (regionId.equals(array.getJSONObject(i).get("id"))) {
//                    JSONObject regionData = array.getJSONObject(i);
//                    region = JSONObject.toJavaObject(regionData, Region.class);
//                    break;
//                }
//            }
//        }


        // 得到config数据
        // 查询对应的参数设置
//        LambdaQueryWrapper<KscTradeProductConfig> queryWrapperConfig = new LambdaQueryWrapper<>();
//        queryWrapperConfig.eq(KscTradeProductConfig::getOrderId, id);
//        //SQL:select * from ksc_trade_product_config where order_id=?
//        List<KscTradeProductConfig> list = kscTradeProductConfigService.list(queryWrapperConfig);
//        System.out.println(list);

        // 数据准备完毕，开始组装DTO
        // userDTO
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUserName(user.getUserName());
//        userDTO.setEmail(user.getEmail());
//        userDTO.setPhone(user.getPhone());
//        userDTO.setAddress(user.getAddress());
        // regionDTO
//        RegionDTO regionDTO = new RegionDTO();
//        regionDTO.setCode(region.getCode());
//        regionDTO.setName(region.getName());
        // configsDTO,可能是一组数据
//        List<ConfigsDTO> configsDTOList = new ArrayList<>();
//        ConfigsDTO configsDTO = new ConfigsDTO();
//        for (KscTradeProductConfig config : list) {
//            configsDTO.setItemNo(config.getItemNo());
//            configsDTO.setItemName(config.getItemName());
//            configsDTO.setUnit(config.getUnit());
//            configsDTO.setValue(config.getValue());
//            configsDTOList.add(configsDTO);
//        }
        try {
            end.await();
            tradeOrderDataDTO.setUpsteam(upsteam);
            tradeOrderDataDTO.setId(userId);
            tradeOrderDataDTO.setPriceValue(priceValue);
            tradeOrderDataDTO.setUserDTO(userDTO);
            tradeOrderDataDTO.setRegionDTO(regionDTO);
            tradeOrderDataDTO.setConfigsDTO(configsDTOList);

            // 程序结束时间
            long endTime = System.currentTimeMillis();
            String descr = (double) (endTime - startTime) / 1000 + "秒";
            tradeOrderDTO.setCode(200);
            tradeOrderDTO.setMsg("ok");
            tradeOrderDTO.setRequestId(requestId);
            tradeOrderDTO.setDescr(descr);
            tradeOrderDTO.setTradeOrderDataDTO(tradeOrderDataDTO);
            Object o = JSONObject.toJSON(tradeOrderDTO);

            // 将数据放入redis中
            redisUtils.set("order_" + id, o);

            return o;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
