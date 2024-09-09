//package com.zzimcong.order.infrastructure.redis;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zzimcong.order.application.dto.ProductQuantity;
//import com.zzimcong.order.application.service.InventoryService;
//import com.zzimcong.order.domain.entity.Order;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.connection.MessageListener;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j(topic = "REDIS-KEY-EXPIRATION-LISTENER")
//@Component
//@RequiredArgsConstructor
//public class RedisKeyExpirationListener implements MessageListener {
//    private static final String TEMP_ORDER_PREFIX = "temp_order:";
//    private static final String BACKUP_PREFIX = "backup:";
//
//    private final InventoryService inventoryService;
//    private final ObjectMapper objectMapper;
//    private final RedisTemplate<String, String> redisTemplate;
//
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        log.info("키 만료 감지: {}", message.toString());
//        String expiredKey = message.toString();
//        if (expiredKey.startsWith(TEMP_ORDER_PREFIX)) {
//            log.info("임시 주문 만료 감지: {}", expiredKey);
//            processExpiredOrder(expiredKey);
//        }
//    }
//
//    private void processExpiredOrder(String expiredKey) {
//        String backupKey = BACKUP_PREFIX + expiredKey;
//        try {
//            String orderJson = redisTemplate.opsForValue().get(backupKey);
//            if (orderJson == null) {
//                log.warn("백업된 주문 정보를 찾을 수 없습니다: {}", expiredKey);
//                return;
//            }
//
//            Order order = objectMapper.readValue(orderJson, Order.class);
//            List<ProductQuantity> productQuantities = order.getOrderItems().stream()
//                    .map(item -> new ProductQuantity(item.getProductId(), item.getQuantity()))
//                    .collect(Collectors.toList());
//
//            // 재고 롤백 실행
//            boolean rollbackSuccess = inventoryService.rollbackInventory(productQuantities);
//
//            if (rollbackSuccess) {
//                // 재고 롤백 성공 시에만 백업 키 삭제
//                redisTemplate.delete(backupKey);
//                log.info("주문 롤백 처리 완료: {}", expiredKey);
//            } else {
//                log.warn("재고 롤백 실패, 백업 유지: {}", expiredKey);
//                // 여기에 재시도 로직 추가 가능
//            }
//        } catch (JsonProcessingException e) {
//            log.error("주문 정보 파싱 중 오류 발생: {}", expiredKey, e);
//        } catch (Exception e) {
//            log.error("주문 롤백 처리 중 오류 발생: {}", expiredKey, e);
//            // 여기에 재시도 로직 추가 가능
//        }
//    }
//}