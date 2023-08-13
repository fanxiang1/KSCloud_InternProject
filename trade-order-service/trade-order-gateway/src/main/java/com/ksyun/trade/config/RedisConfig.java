package com.ksyun.trade.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置
 *
 */
@Configuration
public class RedisConfig {

//    @Bean
//    public JedisConnectionFactory redisConnectionFactory(@Value("spring.redis.host")String host,
//                                                         @Value("spring.redis.port")String port,
//                                                         @Value("spring.redis.password")String password,
//                                                         @Value("spring.redis.host")int db) {
//        JedisConnectionFactory factory = new JedisConnectionFactory();
//        factory.setHostName(host);
//        factory.setPort(Integer.parseInt(port));
//        factory.setPassword(password);
//        factory.setDatabase(db); //设置连接超时时间
//        return factory;
//    }

    /**
     * 初始化RedisTemplate，默认使用的是JDKSerializer的序列方式，效率低，这里配置使用FastJsonRedisSerializer
     *
     * @return redisTemplate
     */

    // @Value("${spring.redis.db}")Integer db
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //key序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //value序列化
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        //Hash Key序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //Hash Value序列表
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.afterPropertiesSet();

        // 切换数据库
//        LettuceConnectionFactory jedisConnectionFactory = (LettuceConnectionFactory) redisTemplate.getConnectionFactory();
//        jedisConnectionFactory.setDatabase(db);
//        redisTemplate.setConnectionFactory(jedisConnectionFactory);
//        jedisConnectionFactory.resetConnection();

        return redisTemplate;
    }
}
