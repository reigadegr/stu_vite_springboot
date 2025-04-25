package com.xxx.MOM;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Redis {
    private final StringRedisTemplate stringRedisTemplate;

    public void set(String key, String value) {
        val ops = stringRedisTemplate.opsForValue();
        ops.set(key, value);
    }

    public String get(String key) {
       val ops = stringRedisTemplate.opsForValue();
        return ops.get(key);
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }
}
