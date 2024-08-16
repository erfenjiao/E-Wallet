package com.example.ewallet.infrastructure.configuration.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

//@Service
@Component
@RequiredArgsConstructor
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void storeToken(String key, String value) {
//        System.out.println("storeToken - userId: " + userId);
        redisTemplate.opsForValue().set(key, value, 1, TimeUnit.DAYS);
//        redisTemplate.opsForValue().set(String.valueOf(userId), token, 1, TimeUnit.DAYS);
    }

    public String getToken(Long userId) {
        return redisTemplate.opsForValue().get("token:" + userId);
//        return redisTemplate.opsForValue().get(String.valueOf(userId));
    }

    public void deleteToken(Long userId) {
        redisTemplate.delete("token:" + userId);
//        redisTemplate.delete(String.valueOf(userId));
    }

    public Boolean hasKey(String token) {
        Boolean hasKey = redisTemplate.hasKey(token);
        Logger logger = Logger.getLogger(RedisService.class.getName());
        logger.info("hasKey: " + hasKey);
        return hasKey;
    }
}
