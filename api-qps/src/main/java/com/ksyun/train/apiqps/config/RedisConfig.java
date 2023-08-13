package com.ksyun.train.apiqps.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean(name = "springSessionDefaultRedisSerializer")
    public GenericJackson2JsonRedisSerializer getGenericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        //创建模板类
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // 配置默认的序列化器
        template.setDefaultSerializer(getGenericJackson2JsonRedisSerializer());
        //redis默认使用JdkSerializationRedisSerializer来进行序列化，造成key是乱码,使用下列进行序列化，修复乱码
        //解决key乱码问题
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        //value不建议使用stringSerializer，因为使用后只能存String类型的值，其他类型不支持
        /**
         * template.setValueSerializer(stringSerializer);
         * template.setHashValueSerializer(stringSerializer);
         */
        return template;
    }
}


