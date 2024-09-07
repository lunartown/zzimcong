package com.zzimcong.order.application.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j(topic = "INVENTORY-SERVICE")
public class InventoryService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;

    private static final String INVENTORY_KEY_PREFIX = "inventory:";
    private static final String RESERVED_KEY_PREFIX = "reserved:";
    private static final String LOCK_KEY_PREFIX = "lock:inventory:";

    private static final long LOCK_WAIT_TIME = 10;
    private static final long LOCK_LEASE_TIME = 5;

    public InventoryService(RedisTemplate<String, Object> redisTemplate, RedissonClient redissonClient) {
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
    }

    public boolean reserveInventory(Long productId, int quantity) {
        String lockKey = LOCK_KEY_PREFIX + productId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 락 획득 시도 (최대 10초 대기, 5초 동안 락 유지)
            if (lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS)) {
                try {
                    String inventoryKey = INVENTORY_KEY_PREFIX + productId;
                    String reservedKey = RESERVED_KEY_PREFIX + productId;

                    Integer currentStock = (Integer) redisTemplate.opsForValue().get(inventoryKey);
                    Integer reservedStock = (Integer) redisTemplate.opsForValue().get(reservedKey);

                    if (currentStock == null || currentStock - (reservedStock != null ? reservedStock : 0) < quantity) {
                        return false;
                    }

                    redisTemplate.opsForValue().increment(reservedKey, quantity);
                    return true;
                } finally {
                    lock.unlock();
                }
            } else {
                log.warn("Failed to acquire lock for product {}", productId);
                return false;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while trying to acquire lock for product {}", productId, e);
            return false;
        }
    }

    public boolean confirmInventoryReduction(Long productId, int quantity) {
        String lockKey = LOCK_KEY_PREFIX + productId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS)) {
                try {
                    String inventoryKey = INVENTORY_KEY_PREFIX + productId;
                    String reservedKey = RESERVED_KEY_PREFIX + productId;

                    Integer currentStock = (Integer) redisTemplate.opsForValue().get(inventoryKey);
                    Integer reservedStock = (Integer) redisTemplate.opsForValue().get(reservedKey);

                    if (currentStock == null || reservedStock == null || reservedStock < quantity) {
                        return false;
                    }

                    redisTemplate.opsForValue().decrement(inventoryKey, quantity);
                    redisTemplate.opsForValue().decrement(reservedKey, quantity);
                    return true;
                } finally {
                    lock.unlock();
                }
            } else {
                log.warn("Failed to acquire lock for product {} during inventory reduction confirmation", productId);
                return false;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while trying to acquire lock for product {} during inventory reduction confirmation", productId, e);
            return false;
        }
    }

    public void rollbackInventory(Long productId, int quantity) {
        String lockKey = LOCK_KEY_PREFIX + productId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS)) {
                try {
                    String reservedKey = RESERVED_KEY_PREFIX + productId;
                    Integer reservedStock = (Integer) redisTemplate.opsForValue().get(reservedKey);
                    if (reservedStock != null && reservedStock >= quantity) {
                        redisTemplate.opsForValue().decrement(reservedKey, quantity);
                    }
                } finally {
                    lock.unlock();
                }
            } else {
                log.warn("Failed to acquire lock for product {} during inventory rollback", productId);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while trying to acquire lock for product {} during inventory rollback", productId, e);
        }
    }
}