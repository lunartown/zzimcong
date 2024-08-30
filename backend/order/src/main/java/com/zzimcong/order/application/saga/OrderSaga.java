package com.zzimcong.order.application.saga;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzimcong.order.application.dto.*;
import com.zzimcong.order.application.mapper.OrderRequestMapper;
import com.zzimcong.order.application.service.InventoryService;
import com.zzimcong.order.common.exception.ErrorCode;
import com.zzimcong.order.common.exception.InternalServerErrorException;
import com.zzimcong.order.common.exception.NotFoundException;
import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderItem;
import com.zzimcong.order.domain.entity.OrderStatus;
import com.zzimcong.order.domain.repository.OrderRepository;
import com.zzimcong.zzimconginventorycore.common.event.InventoryUpdateEvent;
import com.zzimcong.zzimconginventorycore.common.model.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "ORDER-SAGA")
@RequiredArgsConstructor
public class OrderSaga {
    private final InventoryService inventoryService;
    private final OrderRequestMapper orderRequestMapper;
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;


    /***************************************************************************
     주문 준비 프로세스(재고 예약, 임시 주문 생성, 임시 주문 정보 Redis에 저장)
     ***************************************************************************/

    // 주문 준비
    @Transactional
    public OrderPreparationResponse prepareOrder(Long userId, OrderPreparationRequest request) {
        List<ProductQuantity> productQuantities = request.items().stream()
                .map(item -> new ProductQuantity(item.productId(), item.quantity()))
                .collect(Collectors.toList());

        boolean allReserved = inventoryService.reserveInventory(productQuantities);

        if (allReserved) {
            Order tempOrder = createTempOrder(userId, request);
            String tempId = saveTempOrderToRedis(userId, tempOrder);
            return new OrderPreparationResponse(tempId, OrderPreparationStatus.SUCCESS);
        } else {
            return new OrderPreparationResponse(null, OrderPreparationStatus.INVENTORY_SHORTAGE);
        }
    }

    // 임시 주문 생성
    public Order createTempOrder(Long userId, OrderPreparationRequest request) {
        log.info("임시 주문 생성 시작. 사용자 ID: {}", userId);

        Order tempOrder = orderRequestMapper.orderPreperationRequestToOrder(request);
        tempOrder.setUserId(userId);

        return tempOrder;
    }

    // 임시 주문 정보 Redis에 저장
    private String saveTempOrderToRedis(Long userId, Order tempOrder) {
        String tempId = generateUUID();
        String key = "temp_order:" + userId + ":" + tempId;
        String backupKey = "backup:" + key;

        try {
            String orderJson = objectMapper.writeValueAsString(tempOrder);
            Boolean setSuccess = redisTemplate.opsForValue()
                    .setIfAbsent(key, orderJson, Duration.ofMinutes(10));

            if (Boolean.FALSE.equals(setSuccess)) {
                throw new ConcurrentModificationException("동시에 같은 키로 주문 생성 시도");
            }

            redisTemplate.opsForValue().setIfAbsent(backupKey, orderJson, Duration.ofMinutes(11));

            log.info("Temporary order created with ID: {}", tempId);
        } catch (JsonProcessingException e) {
            log.error("Error creating temporary order", e);
            throw new RuntimeException("임시 주문 생성 중 오류가 발생했습니다.");
        }

        return tempId;
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /***************************************************************************
     주문 생성 프로세스(주문 정보 업데이트, 결제 요청)
     ***************************************************************************/

    // 주문 생성
    public void createOrder(Long userId, String uuid, OrderCreationRequest request) {
        log.info("주문 생성 시작. 사용자 ID: {}, UUID: {}", userId, uuid);
        Order order = updateOrderDetails(userId, uuid, request);
        processPayment(userId, uuid, request.paymentDetailsRequest(), order);
    }

    public Order updateOrderDetails(Long userId, String uuid, OrderCreationRequest orderCreationRequest) {
        String key = "temp_order:" + userId + ":" + uuid;

        try {
            // 1. Redis에서 주문 정보 조회
            String orderJson = redisTemplate.opsForValue().get(key);
            if (orderJson == null) {
                throw new NotFoundException(ErrorCode.TEMP_ORDER_NOT_FOUND, "uuid : " + uuid);
            }

            // 2. 주문 정보 업데이트
            Order order = objectMapper.readValue(orderJson, Order.class);
            orderRequestMapper.updateOrderFromRequest(orderCreationRequest, order);
            order.setStatus(OrderStatus.PAYMENT_REQUESTED);

            // 3. 업데이트된 주문 정보를 Redis에 저장
            String updatedOrderJson = objectMapper.writeValueAsString(order);
            Boolean updateSuccess = redisTemplate.opsForValue().setIfPresent(key, updatedOrderJson, Duration.ofMinutes(10));

            if (Boolean.FALSE.equals(updateSuccess)) {
                throw new ConcurrentModificationException("주문 정보가 동시에 수정되었습니다. 다시 시도해주세요.");
            }

            log.info("Order details updated successfully for order: {}", uuid);
            return order;

        } catch (JsonProcessingException e) {
            log.error("Error processing JSON for order: {}", uuid, e);
            throw new InternalServerErrorException(ErrorCode.ORDER_UPDATE_FAILED);
        }
    }

    // 결제 요청
    @Transactional
    public void processPayment(Long userId, String uuid, PaymentDetailsRequest request, Order order) {
        log.info("결제 처리 시작. 사용자 ID: {}, UUID: {}", userId, uuid);
        PaymentRequest paymentRequest = new PaymentRequest(userId, uuid, request, order.getPaymentAmount());
        try {
            kafkaTemplate.send("payment-requests", paymentRequest).get();
            log.info("결제 요청 전송 완료. UUID: {}", uuid);
        } catch (InterruptedException | ExecutionException e) {
            log.error("결제 요청 전송 실패. UUID: {}", uuid, e);
            throw new InternalServerErrorException(ErrorCode.PAYMENT_REQUEST_FAILED, e.getMessage());
        }
    }

    /***************************************************************************
     결제 결과 처리 프로세스(결제 결과 처리, 결제 성공 처리, 보상 트랜잭션)
     ***************************************************************************/

    // 결제 결과 처리
    @KafkaListener(topics = "payment-results")
    public void handlePaymentResult(PaymentResponse result) {
        log.info("결제 결과 수신. UUID: {}, 사용자 ID: {}, 성공 여부: {}", result.getUuid(), result.getUserId(), result.isSuccess());

        String key = "temp_order:" + result.getUserId() + ":" + result.getUuid();
        String orderJson = redisTemplate.opsForValue().get(key);

        if (orderJson == null) {
            log.warn("존재하지 않는 주문에 대한 결제 결과 수신: {}", result.getUuid());
            return;
        }

        try {
            Order order = objectMapper.readValue(orderJson, Order.class);
            log.debug("Redis에서 불러온 주문 정보: {}", order);

            if (result.isSuccess()) {
                processSuccessfulPayment(order);
            } else {
                log.warn("결제 실패. 보상 트랜잭션 시작");
                compensatePaymentFailure(order);
            }

            boolean deleted = Boolean.TRUE.equals(redisTemplate.delete(key));
            log.info("Redis에서 임시 주문 정보 삭제 {}: {}", (deleted ? "성공" : "실패"), key);

        } catch (JsonProcessingException e) {
            log.error("결제 결과 처리 중 JSON 파싱 오류 발생", e);
            throw new InternalServerErrorException(ErrorCode.PAYMENT_RESULT_PROCESSING_FAILED);
        } catch (Exception e) {
            log.error("주문 처리 중 예기치 못한 오류 발생", e);
            throw new InternalServerErrorException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    // 결제 성공 처리
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processSuccessfulPayment(Order order) {
        order.setStatus(OrderStatus.ORDER_COMPLETED);
        // 재고 확인 및 차감 (원자적 처리)
        List<ProductQuantity> productQuantities = order.getOrderItems().stream()
                .map(item -> new ProductQuantity(item.getProductId(), item.getQuantity()))
                .collect(Collectors.toList());
        try {
            boolean allConfirmed = inventoryService.confirmInventoryReduction(productQuantities);
            if (!allConfirmed) {
                throw new InternalServerErrorException(ErrorCode.INVENTORY_UPDATE_FAILED,
                        "Failed to update inventory for one or more products");
            }

            // 주문 아이템 설정 및 Kafka 메시지 전송
            for (OrderItem item : order.getOrderItems()) {
                item.setOrder(order);
                log.debug("주문 아이템 설정: {}", item);

                // 실제 데이터베이스의 재고 차감 (Kafka를 통해 비동기적으로 처리)
                kafkaTemplate.send("inventory-update", new InventoryUpdateEvent(item.getProductId(), item.getQuantity()));
            }

            log.info("주문 정보: {}", order);

            orderRepository.save(order);
            log.info("주문 저장 성공. 주문 ID: {}", order.getId());
        } catch (DataAccessException e) {
            log.error("주문 저장 중 데이터베이스 오류 발생", e);
            throw new InternalServerErrorException(ErrorCode.ORDER_SAVE_FAILED);
        } catch (Exception e) {
            log.error("주문 처리 중 예기치 못한 오류 발생", e);
            throw new InternalServerErrorException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    // 보상 트랜잭션: 결제 실패 시 재고 회수
    private void compensatePaymentFailure(Order order) {
        log.info("결제 실패에 대한 보상 트랜잭션 시작. 주문 ID: {}", order.getId());
        try {
            List<ProductQuantity> productQuantities = order.getOrderItems().stream()
                    .map(item -> new ProductQuantity(item.getProductId(), item.getQuantity()))
                    .collect(Collectors.toList());
            inventoryService.rollbackInventory(productQuantities);
            log.info("주문에 대한 재고 롤백 성공. 주문 ID: {}", order.getId());
        } catch (Exception e) {
            log.error("주문에 대한 재고 롤백 실패. 주문 ID: {}, 오류: {}", order.getId(), e.getMessage(), e);
            // 추가적인 오류 처리 로직
            handleRollbackFailure(order);
        }
        log.info("보상 트랜잭션 완료");
    }

    private void handleRollbackFailure(Order order) {
        // 롤백 실패에 대한 추가 처리
        log.warn("주문 ID: {}에 대한 수동 처리가 필요합니다.", order.getId());
        // 예: 관리자에게 알림
//        notificationService.notifyAdmin("재고 롤백 실패",
//                String.format("주문 ID: %s에 대한 재고 롤백이 실패했습니다. 수동 처리가 필요합니다.", order.getId()));
        // 수동 처리를 위한 큐에 추가
//        manualProcessingQueue.addOrder(order);
    }
}