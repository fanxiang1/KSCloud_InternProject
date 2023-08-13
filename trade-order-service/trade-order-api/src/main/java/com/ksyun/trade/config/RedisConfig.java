package com.ksyun.trade.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.io.Serializable;

@Configuration
public class RedisConfig {

    @Bean(name = "springSessionDefaultRedisSerializer")
    public GenericJackson2JsonRedisSerializer getGenericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
//        //创建模板类
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(factory);
//        // 配置默认的序列化器
//        template.setDefaultSerializer(getGenericJackson2JsonRedisSerializer());
//        //redis默认使用JdkSerializationRedisSerializer来进行序列化，造成key是乱码,使用下列进行序列化，修复乱码
//        //解决key乱码问题
//        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
//        template.setKeySerializer(stringSerializer);
//        template.setHashKeySerializer(stringSerializer);
//        //value不建议使用stringSerializer，因为使用后只能存String类型的值，其他类型不支持
//        /**
//         * template.setValueSerializer(stringSerializer);
//         * template.setHashValueSerializer(stringSerializer);
//         */
//        return template;
//    }


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
    // @Value("${spring.redis.db}")Integer db

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用 GenericFastJsonRedisSerializer 替换默认序列化
        GenericFastJsonRedisSerializer genericFastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        // 设置key和value的序列化规则
        redisTemplate.setKeySerializer(new GenericToStringSerializer<>(Object.class));
        redisTemplate.setValueSerializer(genericFastJsonRedisSerializer);
        // 设置hashKey和hashValue的序列化规则
        redisTemplate.setHashKeySerializer(new GenericToStringSerializer<>(Object.class));
        redisTemplate.setHashValueSerializer(genericFastJsonRedisSerializer);
        // 设置支持事物
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();

        // 切换数据库
//        LettuceConnectionFactory jedisConnectionFactory = (LettuceConnectionFactory) redisTemplate.getConnectionFactory();
//        jedisConnectionFactory.setDatabase(db);
//        redisTemplate.setConnectionFactory(jedisConnectionFactory);
//        jedisConnectionFactory.resetConnection();

        return redisTemplate;
    }
}


