package com.xxx.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig<T> {
    @Bean
    public RedisTemplate<String, T> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        val str = new StringRedisSerializer();
        val json = new GenericJackson2JsonRedisSerializer();
        val jdk = new JdkSerializationRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(str);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(str);

        // value序列化方式采用 String
        template.setValueSerializer(json);
        // hash的value序列化方式采用String
        template.setHashValueSerializer(json);

        template.afterPropertiesSet();
        return template;
    }
}
