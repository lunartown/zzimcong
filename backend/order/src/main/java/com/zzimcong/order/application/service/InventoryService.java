package com.zzimcong.order.application.service;

import com.zzimcong.order.application.dto.ProductQuantity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j(topic = "INVENTORY-SERVICE")
@RequiredArgsConstructor
public class InventoryService {
    private final RedisTemplate<String, String> redisTemplate;

    // 재고 예약 Lua 스크립트
    private static final String RESERVE_INVENTORY_SCRIPT =
            """
                    local inventoryKey = KEYS[1]
                    local reservedKey = KEYS[2]
                    local quantity = tonumber(ARGV[1])
                    local currentStock = redis.call('get', inventoryKey)
                    if currentStock == false or tonumber(currentStock) < quantity then
                        return 0
                    end
                    redis.call('incrby', reservedKey, quantity)
                    return 1
                    """;

    // 재고 감소 확인 Lua 스크립트
    private static final String CONFIRM_INVENTORY_REDUCTION_SCRIPT =
            """
                    local inventoryKey = KEYS[1]
                    local reservedKey = KEYS[2]
                    local quantity = tonumber(ARGV[1])
                    local currentStock = redis.call('get', inventoryKey)
                    local reservedStock = redis.call('get', reservedKey)
                    if currentStock == false or tonumber(currentStock) < quantity or reservedStock == false or tonumber(reservedStock) < quantity then
                        return 0
                    end
                    redis.call('decrby', inventoryKey, quantity)
                    redis.call('decrby', reservedKey, quantity)
                    return 1
                    """;

    // 재고 롤백 Lua 스크립트
    private static final String ROLLBACK_INVENTORY_SCRIPT =
            """
                    local reservedKey = KEYS[1]
                    local quantity = tonumber(ARGV[1])
                    local reservedStock = redis.call('get', reservedKey)
                    if reservedStock ~= false then
                        redis.call('decrby', reservedKey, quantity)
                    end
                    return 1
                    """;

    public boolean reserveInventory(List<ProductQuantity> productQuantities) {
        for (ProductQuantity pq : productQuantities) {
            List<String> keys = List.of("inventory:" + pq.productId(), "reserved:" + pq.productId());
            try {
                Long result = redisTemplate.execute(
                        RedisScript.of(RESERVE_INVENTORY_SCRIPT, Long.class),
                        keys,
                        String.valueOf(pq.quantity())
                );
                if (result == null || result != 1) {
                    log.warn("상품 {} 재고 예약 실패: 요청 수량={}", pq.productId(), pq.quantity());
                    return false;
                }
            } catch (Exception e) {
                log.error("상품 {} 재고 예약 중 오류 발생", pq.productId(), e);
                return false;
            }
        }
        log.info("재고 예약 성공: {}", productQuantities);
        return true;
    }

    public boolean confirmInventoryReduction(List<ProductQuantity> productQuantities) {
        for (ProductQuantity pq : productQuantities) {
            List<String> keys = List.of("inventory:" + pq.productId(), "reserved:" + pq.productId());
            try {
                Long result = redisTemplate.execute(
                        RedisScript.of(CONFIRM_INVENTORY_REDUCTION_SCRIPT, Long.class),
                        keys,
                        String.valueOf(pq.quantity())
                );
                if (result == null || result != 1) {
                    log.warn("상품 {} 재고 감소 확인 실패: 요청 수량={}", pq.productId(), pq.quantity());
                    return false;
                }
            } catch (Exception e) {
                log.error("상품 {} 재고 감소 확인 중 오류 발생", pq.productId(), e);
                return false;
            }
        }
        log.info("재고 감소 확인 성공: {}", productQuantities);
        return true;
    }

    public boolean rollbackInventory(List<ProductQuantity> productQuantities) {
        for (ProductQuantity pq : productQuantities) {
            List<String> keys = List.of("reserved:" + pq.productId());
            try {
                Long result = redisTemplate.execute(
                        RedisScript.of(ROLLBACK_INVENTORY_SCRIPT, Long.class),
                        keys,
                        String.valueOf(pq.quantity())
                );
                if (result == null || result != 1) {
                    log.warn("상품 {} 재고 롤백 실패: 요청 수량={}", pq.productId(), pq.quantity());
                    return false;
                }
            } catch (Exception e) {
                log.error("상품 {} 재고 롤백 중 오류 발생", pq.productId(), e);
                return false;
            }
        }
        log.info("재고 롤백 성공: {}", productQuantities);
        return true;
    }
}