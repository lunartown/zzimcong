package com.zzimcong.order.application.service;

import com.zzimcong.order.application.dto.ProductQuantity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "INVENTORY-SERVICE")
@RequiredArgsConstructor
public class InventoryService {
    private final RedisTemplate<String, String> redisTemplate;

    // 재고 예약 Lua 스크립트 (기존과 동일)
    private static final String RESERVE_INVENTORY_SCRIPT =
            "local success = true " +
                    "local inventories = {} " +
                    "for i = 1, #KEYS do " +
                    "   local inventoryKey = KEYS[i] " +
                    "   local quantity = tonumber(ARGV[i]) " +
                    "   local currentStock = redis.call('get', inventoryKey) " +
                    "   if currentStock == false or tonumber(currentStock) < quantity then " +
                    "       success = false " +
                    "       break " +
                    "   end " +
                    "   inventories[i] = {inventoryKey, currentStock, quantity} " +
                    "end " +
                    "if success then " +
                    "   for i = 1, #inventories do " +
                    "       local inv = inventories[i] " +
                    "       redis.call('decrby', inv[1], inv[3]) " +
                    "   end " +
                    "   return 1 " +
                    "else " +
                    "   return 0 " +
                    "end";

    // 재고 감소 확인 Lua 스크립트
    private static final String CONFIRM_INVENTORY_REDUCTION_SCRIPT =
            "local success = true " +
                    "for i = 1, #KEYS do " +
                    "   local inventoryKey = KEYS[i] " +
                    "   local quantity = tonumber(ARGV[i]) " +
                    "   local currentStock = redis.call('get', inventoryKey) " +
                    "   if currentStock == false or tonumber(currentStock) < 0 then " +
                    "       success = false " +
                    "       break " +
                    "   end " +
                    "end " +
                    "return success and 1 or 0";

    // 재고 롤백 Lua 스크립트
    private static final String ROLLBACK_INVENTORY_SCRIPT =
            "local success = true " +
                    "for i = 1, #KEYS do " +
                    "   local inventoryKey = KEYS[i] " +
                    "   local quantity = tonumber(ARGV[i]) " +
                    "   local currentStock = redis.call('get', inventoryKey) " +
                    "   if currentStock == false then " +
                    "       redis.call('set', inventoryKey, quantity) " +
                    "   else " +
                    "       redis.call('incrby', inventoryKey, quantity) " +
                    "   end " +
                    "end " +
                    "return 1";

    public boolean reserveInventory(List<ProductQuantity> productQuantities) {
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();

        for (ProductQuantity pq : productQuantities) {
            keys.add("inventory:" + pq.productId());
            values.add(String.valueOf(pq.quantity()));
        }

        try {
            Long result = redisTemplate.execute(
                    RedisScript.of(RESERVE_INVENTORY_SCRIPT, Long.class),
                    keys,
                    values.toArray()
            );

            boolean success = (result != null && result == 1);
            if (success) {
                log.info("재고 예약 성공: {}", productQuantities);
            } else {
                log.warn("재고 예약 실패: {}", productQuantities);
            }
            return success;
        } catch (Exception e) {
            log.error("재고 예약 중 오류 발생: {}", productQuantities, e);
            return false;
        }
    }

    public boolean confirmInventoryReduction(List<ProductQuantity> productQuantities) {
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();

        for (ProductQuantity pq : productQuantities) {
            keys.add("inventory:" + pq.productId());
            values.add(String.valueOf(pq.quantity()));
        }

        try {
            Long result = redisTemplate.execute(
                    RedisScript.of(CONFIRM_INVENTORY_REDUCTION_SCRIPT, Long.class),
                    keys,
                    values.toArray()
            );

            boolean success = (result != null && result == 1);
            if (success) {
                log.info("재고 감소 확인 성공: {}", productQuantities);
            } else {
                log.warn("재고 감소 확인 실패: {}", productQuantities);
            }
            return success;
        } catch (Exception e) {
            log.error("재고 감소 확인 중 오류 발생: {}", productQuantities, e);
            return false;
        }
    }

    public boolean rollbackInventory(List<ProductQuantity> productQuantities) {
        List<String> keys = new ArrayList<>();
        List<String> quantities = new ArrayList<>();

        for (ProductQuantity pq : productQuantities) {
            keys.add("inventory:" + pq.productId());
            quantities.add(String.valueOf(pq.quantity()));
        }

        try {
            Long result = redisTemplate.execute(
                    RedisScript.of(ROLLBACK_INVENTORY_SCRIPT, Long.class),
                    keys,
                    quantities.toArray()
            );

            boolean success = (result != null && result == 1);
            if (success) {
                log.info("재고 롤백 성공: {}", productQuantities);
            } else {
                log.warn("재고 롤백 실패: {}", productQuantities);
            }
            return success;
        } catch (Exception e) {
            log.error("재고 롤백 중 오류 발생: {}", productQuantities, e);
            return false;
        }
    }
}