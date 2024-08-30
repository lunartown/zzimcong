package com.zzimcong.order.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzimcong.order.application.dto.ProductQuantity;
import com.zzimcong.order.application.service.InventoryService;
import com.zzimcong.order.domain.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "REDIS-KEY-EXPIRATION-LISTENER")
@Component
@RequiredArgsConstructor
public class RedisKeyExpirationListener implements MessageListener {

    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if (expiredKey.startsWith("temp_order:")) {
            try {
                // 백업된 주문 정보 키
                String backupKey = "backup:" + expiredKey;
                String orderJson = redisTemplate.opsForValue().get(backupKey);

                if (orderJson != null) {
                    Order order = objectMapper.readValue(orderJson, Order.class);
                    List<ProductQuantity> productQuantities = order.getOrderItems().stream()
                            .map(item -> new ProductQuantity(item.getProductId(), item.getQuantity()))
                            .collect(Collectors.toList());
                    
                    inventoryService.rollbackInventory(productQuantities);

                    // 백업 키 삭제
                    redisTemplate.delete(backupKey);
                } else {
                    log.warn("백업된 주문 정보를 찾을 수 없습니다: {}", expiredKey);
                }
            } catch (Exception e) {
                log.error("주문 롤백 처리 중 오류 발생: {}", expiredKey, e);
            }
        }
    }

}