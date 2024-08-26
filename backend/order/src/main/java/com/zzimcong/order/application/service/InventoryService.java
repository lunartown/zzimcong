package com.zzimcong.order.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j(topic = "INVENTORY-SERVICE")
public class InventoryService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisLock redisLock;

    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_BACKOFF = 100L; // 초기 백오프 시간 (밀리초)

    public InventoryService(RedisTemplate<String, Object> redisTemplate, RedisLock redisLock) {
        this.redisTemplate = redisTemplate;
        this.redisLock = redisLock;
    }

    public boolean reserveInventory(Long productId, int quantity) {
        String inventoryKey = "inventory:" + productId;
        String reservedKey = "reserved:" + productId;

        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                // 락 획득 시도 시간을 5초로 줄임
                boolean result = redisLock.executeWithLock(inventoryKey, 5000, () -> {
                    Integer currentStock = (Integer) redisTemplate.opsForValue().get(inventoryKey);
                    Integer reservedStock = (Integer) redisTemplate.opsForValue().get(reservedKey);

                    if (currentStock == null || currentStock - (reservedStock != null ? reservedStock : 0) < quantity) {
                        return false;
                    }

                    redisTemplate.opsForValue().increment(reservedKey, quantity);
                    return true;
                });

                if (result) {
                    return true; // 성공적으로 재고 예약
                }
            } catch (Exception e) {
                log.error("Failed to reserve inventory for product {} on attempt {}", productId, attempt + 1, e);
            }

            // 백오프 로직
            try {
                long backoffTime = INITIAL_BACKOFF * (long) Math.pow(2, attempt);
                TimeUnit.MILLISECONDS.sleep(backoffTime);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        log.warn("Failed to reserve inventory for product {} after {} attempts", productId, MAX_RETRIES);
        return false; // 모든 재시도 실패
    }

    public boolean confirmInventoryReduction(Long productId, int quantity) {
        String inventoryKey = "inventory:" + productId;
        String reservedKey = "reserved:" + productId;

        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                Boolean result = redisTemplate.execute(new SessionCallback<Boolean>() {
                    @Override
                    public Boolean execute(RedisOperations operations) throws DataAccessException {
                        operations.watch(inventoryKey);
                        operations.watch(reservedKey);

                        Integer currentStock = (Integer) operations.opsForValue().get(inventoryKey);
                        Integer reservedStock = (Integer) operations.opsForValue().get(reservedKey);

                        if (currentStock == null || reservedStock == null || reservedStock < quantity) {
                            return false;
                        }

                        operations.multi();
                        operations.opsForValue().decrement(inventoryKey, quantity);
                        operations.opsForValue().decrement(reservedKey, quantity);
                        List<Object> execResult = operations.exec();
                        return !execResult.isEmpty();
                    }
                });

                if (Boolean.TRUE.equals(result)) {
                    return true; // 재고 감소 확정 성공
                }
            } catch (Exception e) {
                log.error("Failed to confirm inventory reduction for product {} on attempt {}", productId, attempt + 1, e);
            }

            // 백오프 로직
            try {
                long backoffTime = INITIAL_BACKOFF * (long) Math.pow(2, attempt);
                TimeUnit.MILLISECONDS.sleep(backoffTime);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        log.warn("Failed to confirm inventory reduction for product {} after {} attempts", productId, MAX_RETRIES);
        return false; // 모든 재시도 실패
    }

    public void rollbackInventory(Long productId, int quantity) {
        String reservedKey = "reserved:" + productId;

        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                redisLock.executeWithLock(reservedKey, 5000, () -> {
                    Integer reservedStock = (Integer) redisTemplate.opsForValue().get(reservedKey);
                    if (reservedStock != null && reservedStock >= quantity) {
                        redisTemplate.opsForValue().decrement(reservedKey, quantity);
                    }
                    return null;
                });
                return; // 성공적으로 롤백
            } catch (Exception e) {
                log.error("Failed to rollback inventory for product {} on attempt {}", productId, attempt + 1, e);
            }

            // 백오프 로직
            try {
                long backoffTime = INITIAL_BACKOFF * (long) Math.pow(2, attempt);
                TimeUnit.MILLISECONDS.sleep(backoffTime);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        log.warn("Failed to rollback inventory for product {} after {} attempts", productId, MAX_RETRIES);
    }
}