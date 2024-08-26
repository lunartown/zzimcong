package com.zzimcong.order.application.queue;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderSaveQueue {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String ORDER_SAVE_QUEUE_KEY = "order:save:queue";

    public OrderSaveQueue(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addToSaveQueue(String orderJson) {
        redisTemplate.opsForList().rightPush(ORDER_SAVE_QUEUE_KEY, orderJson);
    }

    public List<String> getOrderSaveBatch(int batchSize) {
        return redisTemplate.opsForList().leftPop(ORDER_SAVE_QUEUE_KEY, batchSize);
    }
}