package com.zzimcong.order.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzimcong.order.application.queue.OrderSaveQueue;
import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderItem;
import com.zzimcong.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderSaveBatchService {
    @Autowired
    @Qualifier("applicationTaskExecutor")
    private Executor asyncExecutor;
    private final OrderSaveQueue orderSaveQueue;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String FAILED_ORDER_SAVE_KEY = "failed:order:save";
    private static final int MAX_RETRIES = 3;
    private static final int BATCH_SIZE = 50;

    @Scheduled(fixedRate = 50000) // 5초마다 실행
    @Transactional
    public void processSaveBatch() {
        List<String> batch = orderSaveQueue.getOrderSaveBatch(BATCH_SIZE);
        Map<Order, Integer> ordersToSave = new HashMap<>();

        for (String orderJson : batch) {
            try {
                Order order = objectMapper.readValue(orderJson, Order.class);
                ordersToSave.put(order, 0); // 초기 재시도 횟수는 0
            } catch (JsonProcessingException e) {
                log.error("주문 JSON 파싱 중 오류 발생: {}", orderJson, e);
                handleFailedOrder(orderJson);
            }
        }

        if (!ordersToSave.isEmpty()) {
            saveOrdersWithRetry(ordersToSave);
        }
    }

//    private void saveOrdersWithRetry(Map<Order, Integer> ordersToSave) {
//        List<Order> successfulOrders = new ArrayList<>();
//        Map<Order, Integer> failedOrders = new HashMap<>();
//
//        for (Map.Entry<Order, Integer> entry : ordersToSave.entrySet()) {
//            Order order = entry.getKey();
//            int retryCount = entry.getValue();
//
//            try {
////                // 중복 체크 (예: 주문 ID로 이미 저장된 주문인지 확인)
////                if (orderRepository.existsById(order.getId())) {
////                    log.warn("주문 ID {}는 이미 저장되어 있습니다. 건너뜁니다.", order.getId());
////                    continue;
////                }
//
//                for (OrderItem item : order.getOrderItems()) {
//                    item.setOrder(order);
//                }
//
//                order = orderRepository.save(order);
//                successfulOrders.add(order);
//                log.info("주문 저장 성공: {}", order.getId());
//            } catch (DataIntegrityViolationException e) {
//                log.warn("주문 저장 중 데이터 무결성 위반 발생: {}", order.getId(), e);
//                // 데이터 무결성 위반은 재시도하지 않고 바로 실패 처리
//                handleFailedOrderWithExceptionHandling(order);
//            } catch (Exception e) {
//                log.error("주문 저장 중 오류 발생: {}", order.getId(), e);
//                if (retryCount < MAX_RETRIES) {
//                    failedOrders.put(order, retryCount + 1);
//                } else {
//                    handleFailedOrderWithExceptionHandling(order);
//                }
//            }
//        }
//
//        log.info("배치 처리 결과 - 성공: {}, 실패: {}", successfulOrders.size(), failedOrders.size());
//
//        // 실패한 주문 재시도
//        if (!failedOrders.isEmpty()) {
//            saveOrdersWithRetry(failedOrders);
//        }
//    }

    private void saveOrdersWithRetry(Map<Order, Integer> ordersToSave) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (Map.Entry<Order, Integer> entry : ordersToSave.entrySet()) {
            Order order = entry.getKey();
            int retryCount = entry.getValue();

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    for (OrderItem item : order.getOrderItems()) {
                        item.setOrder(order);
                    }
                    Order savedOrder = orderRepository.save(order);
                    log.info("주문 저장 성공: {}", savedOrder.getId());
                } catch (DataIntegrityViolationException e) {
                    log.warn("주문 저장 중 데이터 무결성 위반 발생: {}", order.getId(), e);
                    handleFailedOrderWithExceptionHandling(order);
                } catch (Exception e) {
                    log.error("주문 저장 중 오류 발생: {}", order.getId(), e);
                    if (retryCount < MAX_RETRIES) {
                        // 재시도 로직
                        ordersToSave.put(order, retryCount + 1);
                    } else {
                        handleFailedOrderWithExceptionHandling(order);
                    }
                }
            }, asyncExecutor);

            futures.add(future);
        }

        // 모든 비동기 작업이 완료될 때까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 재시도가 필요한 주문들을 다시 처리
        Map<Order, Integer> ordersToRetry = ordersToSave.entrySet().stream()
                .filter(e -> e.getValue() < MAX_RETRIES)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (!ordersToRetry.isEmpty()) {
            //무한 시도 중
            saveOrdersWithRetry(ordersToRetry);
        }
    }

    private void handleFailedOrderWithExceptionHandling(Order order) {
        try {
            String orderJson = objectMapper.writeValueAsString(order);
            handleFailedOrder(orderJson);
        } catch (JsonProcessingException e) {
            log.error("실패한 주문을 JSON으로 변환하는 데 실패: {}", order.getId(), e);
            // JSON 변환 실패 시 추가적인 에러 처리 로직
            // 예: 관리자에게 알림, 에러 모니터링 시스템에 기록 등
        }
    }

    private void handleFailedOrder(String orderJson) {
        try {
            redisTemplate.opsForList().rightPush(FAILED_ORDER_SAVE_KEY, orderJson);
            log.warn("실패한 주문을 별도의 큐에 저장: {}", orderJson);
        } catch (Exception e) {
            log.error("실패한 주문을 별도의 큐에 저장하는 데 실패: {}", orderJson, e);
        }
    }

    @Scheduled(cron = "0 0 * * * *") // 매시간 실행
    public void processFailedOrders() {
        List<String> failedOrders = redisTemplate.opsForList().range(FAILED_ORDER_SAVE_KEY, 0, -1);
        if (failedOrders != null && !failedOrders.isEmpty()) {
            Map<Order, Integer> ordersToRetry = new HashMap<>();
            for (String orderJson : failedOrders) {
                try {
                    Order order = objectMapper.readValue(orderJson, Order.class);
                    ordersToRetry.put(order, 0);
                } catch (JsonProcessingException e) {
                    log.error("실패한 주문 JSON 파싱 중 오류 발생: {}", orderJson, e);
                }
            }
            saveOrdersWithRetry(ordersToRetry);
            redisTemplate.delete(FAILED_ORDER_SAVE_KEY);
        }
    }
}