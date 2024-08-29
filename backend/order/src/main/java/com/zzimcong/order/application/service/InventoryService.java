package com.zzimcong.order.application.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j(topic = "INVENTORY-SERVICE")
public class InventoryService {
    private final RedissonClient redissonClient;
    private static final long LOCK_WAIT_TIME = 5000L; // 5초
    private static final long LOCK_LEASE_TIME = 10000L; // 10초

    public InventoryService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public boolean reserveInventory(Long productId, int quantity) {
        String lockKey = "lock:inventory:" + productId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (!lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.MILLISECONDS)) {
                log.warn("재고 예약을 위한 락 획득 실패 (상품 ID: {})", productId);
                return false;
            }

            String inventoryKey = "inventory:" + productId;
            String reservedKey = "reserved:" + productId;

            RBucket<Integer> inventoryBucket = redissonClient.getBucket(inventoryKey);
            RBucket<Integer> reservedBucket = redissonClient.getBucket(reservedKey);

            Integer currentStock = inventoryBucket.get();
            Integer reservedStock = reservedBucket.get();

            if (currentStock == null || currentStock - (reservedStock != null ? reservedStock : 0) < quantity) {
                return false;
            }

            reservedBucket.set(reservedStock == null ? quantity : reservedStock + quantity);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("재고 예약 중 인터럽트 발생 (상품 ID: {})", productId, e);
            return false;
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public boolean confirmInventoryReduction(Long productId, int quantity) {
        String lockKey = "lock:inventory:" + productId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (!lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.MILLISECONDS)) {
                log.warn("재고 감소 확인을 위한 락 획득 실패 (상품 ID: {})", productId);
                return false;
            }

            String inventoryKey = "inventory:" + productId;
            String reservedKey = "reserved:" + productId;

            RBucket<Integer> inventoryBucket = redissonClient.getBucket(inventoryKey);
            RBucket<Integer> reservedBucket = redissonClient.getBucket(reservedKey);

            Integer currentStock = inventoryBucket.get();
            Integer reservedStock = reservedBucket.get();

            if (currentStock == null || reservedStock == null || reservedStock < quantity) {
                return false;
            }

            inventoryBucket.set(currentStock - quantity);
            reservedBucket.set(reservedStock - quantity);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("재고 감소 확인 중 인터럽트 발생 (상품 ID: {})", productId, e);
            return false;
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public void rollbackInventory(Long productId, int quantity) {
        String lockKey = "lock:inventory:" + productId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (!lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.MILLISECONDS)) {
                log.warn("재고 롤백을 위한 락 획득 실패 (상품 ID: {})", productId);
                return;
            }

            String reservedKey = "reserved:" + productId;
            RBucket<Integer> reservedBucket = redissonClient.getBucket(reservedKey);
            Integer reservedStock = reservedBucket.get();

            if (reservedStock != null && reservedStock >= quantity) {
                reservedBucket.set(reservedStock - quantity);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("재고 롤백 중 인터럽트 발생 (상품 ID: {})", productId, e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}