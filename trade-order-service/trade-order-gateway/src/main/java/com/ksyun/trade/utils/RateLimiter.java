package com.ksyun.trade.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RateLimiter {

    /**
     * $lb = LeakyBucket
     **/
    private static final String GROUP_KEY = "$lb";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * KEYS[1] = GROUP_KEY
     * KEYS[2] = water    上次水量
     * KEYS[3] = time     上次请求时间(s)
     * ARGV[1] = capacity 最大蓄水量
     * ARGV[2] = rate     水流速率/s
     * ARGV[3] = now_time 当前请求时间(s)
     * ARGV[4] = acquire  请求个数
     */
    private static final String LEAKEY_BUCKET_SCRIPT = " local capacity = tonumber(ARGV[1])" +
            " local rate = tonumber(ARGV[2])" +
            " local now = tonumber(ARGV[3])" +
            " local acquire = tonumber(ARGV[4])" +
            // 上次请求后桶中水量
            " local water = tonumber(redis.call('hget', KEYS[1] , KEYS[2]) or 0) " +
            // 上次请求时间(/s)
            " local time = tonumber(redis.call('hget', KEYS[1] , KEYS[3]) or now) " +
            // 当前桶中的水量 = 上次请求后桶中水量 - 上次到现在流出的水量
            " water = math.max(0, water - (now - time) * rate)" +
            // 设置请求时间(/s)
            " redis.call('hset' , KEYS[1] ,KEYS[3] , now)" +
            // 判断桶中水是否满了
            " if (water + acquire <= capacity) then" +
            // 未满注水
            " redis.call('hset' , KEYS[1] , KEYS[2] , water + acquire)" +
            " return 1" +
            " else" +
            // 满了直接返回
            " return 0" +
            " end";


    public Boolean limit(Long capacity, Long rate, String key) {
        return limit(capacity, rate, 1L, key);
    }

    public Boolean limit(Long capacity, Long rate, Long acquire, String key) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LEAKEY_BUCKET_SCRIPT, Long.class);
        List<String> keys = Arrays.asList(GROUP_KEY, key + ":water", key + ":time");
        Long count = redisTemplate.execute(redisScript, keys, capacity, rate, System.currentTimeMillis() / 1000, acquire);
        return count==1;
    }

//    protected RedisSerializer keySerializer = new StringRedisSerializer();
//    protected RedisSerializer valueSerializer = new Jackson2JsonRedisSerializer(Object.class);
//
//    private static final String LUA_SCRIPT ="local c\n" +
//            " c = redis.call('get',KEYS[1]) \n" +
//            " if c and tonumber(c) > tonumber(ARGV[1]) then \n" +
//            "     return c;\n" +
//            " end \n" +
//            " c = redis.call('incr',KEYS[1]) \n" +
//            " if tonumber(c) == 1 then \n" +
//            "     redis.call('expire',KEYS[1],ARGV[2]) \n" +
//            " end \n" +
//            " return c;";
//    @Autowired
//    protected StringRedisTemplate stringRedisTemplate;
//
//    public boolean tryAccess(String key,long limitCount,int seconds) {
//        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LUA_SCRIPT, Long.class);
//        List<String> keys = new ArrayList<>(2);
//        keys.add(key);
//        Long count = stringRedisTemplate.execute(redisScript
//                , this.valueSerializer
//                , this.keySerializer
//                , keys
//                , limitCount
//                ,seconds);
//        return count <= limitCount;
//    }
}
