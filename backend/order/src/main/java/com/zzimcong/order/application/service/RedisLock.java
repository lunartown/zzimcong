package com.zzimcong.order.application.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class RedisLock {
    private final StringRedisTemplate redisTemplate;
    private final String lockKey = "inventory_lock:";

    public RedisLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> T executeWithLock(String key, long timeoutMillis, Supplier<T> action) {
        String lockKey = this.lockKey + key;
        boolean locked = false;
        try {
            locked = acquireLock(lockKey, timeoutMillis);
            if (locked) {
                return action.get();
            } else {
                throw new RuntimeException("Failed to acquire lock for key: " + key);
            }
        } finally {
            if (locked) {
                releaseLock(lockKey);
            }
        }
    }

    private boolean acquireLock(String key, long timeoutMillis) {
        long expiryTime = System.currentTimeMillis() + timeoutMillis + 1;
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(expiryTime));
        return Boolean.TRUE.equals(acquired);
    }

    private void releaseLock(String key) {
        redisTemplate.delete(key);
    }
}