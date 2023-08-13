package com.ksyun.train.apiqps.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksyun.train.apiqps.annotation.RateLimiterAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


@Component
public class LimitInterceptor implements HandlerInterceptor {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String getMyProperty=System.getProperty("config_path");

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            if (handler instanceof HandlerMethod) {
                HandlerMethod hm = (HandlerMethod) handler;
                RateLimiterAnnotation accessLimit = hm.getMethodAnnotation(RateLimiterAnnotation.class);
                if (null == accessLimit) {
                    return true;
                }
                long seconds = accessLimit.ttl();
                //时间内的 最大次数
                AtomicInteger maxCount = new AtomicInteger(accessLimit.maxCount());

                String name = ((HandlerMethod) handler).getMethod().getName();
                // 存储key
                String key = request.getHeader("X-KSC-ACCOUNT-ID")+name;

                // 得到解析文件地址并解析内容
                String config_path = System.getProperty("config_path");
                ClassPathResource resource = new ClassPathResource(config_path);
                File file = resource.getFile();
                Stream<String> lines = Files.lines(file.toPath());
                // 随机行顺序进行数据处理
                lines.forEach(ele -> {
                    String[] split = ele.split("=");
                    JSONObject jsonObject = JSONObject.parseObject(split[1]);
                    if(key.equals(split[0]+name)){
                        int o = (int)jsonObject.get(name);
                        maxCount.set(o);
                    }
                });



                // 已经访问的次数
                Integer count = (Integer) redisTemplate.opsForValue().get(key);
                if (null == count || -1 == count) {
                    // 设置10秒内有效
                    redisTemplate.opsForValue().set(key, 1, seconds, TimeUnit.SECONDS);
                    count=1;
                    log.info("检测到目前ip对接口={}已经访问的次数{}", request.getServletPath(), count);
                    return true;
                }
                if (count < maxCount.get()) {
                    count++;
                    redisTemplate.opsForValue().set(key,count,seconds,TimeUnit.SECONDS);
                    log.info("检测到目前ip对接口={}已经访问的次数{}", request.getServletPath(), count);
//                    redisTemplate.opsForValue().increment(key);
                    return true;
                }
                log.warn("请求过于频繁请稍后再试");
                returnData(response);
                return false;

            }
        } catch (Exception e) {
            log.warn("请求过于频繁请稍后再试");
            e.printStackTrace();
        }
        return true;
    }

    // 返回错误码
    public void returnData(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        ObjectMapper objectMapper = new ObjectMapper();
        //这里传提示语可以改成自己项目的返回数据封装的类
        response.getWriter().println(objectMapper.writeValueAsString("request too much"));
        return;
    }

}
