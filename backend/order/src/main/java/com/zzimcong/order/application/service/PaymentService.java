package com.zzimcong.order.application.service;

import com.zzimcong.order.application.dto.PaymentRequest;
import com.zzimcong.order.application.dto.PaymentResponse;
import com.zzimcong.order.application.dto.RefundResponse;
import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.zzimconginventorycore.common.model.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j(topic = "payment-service")
@Service
public class PaymentService {
    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;
    private final Set<String> processedPayments = ConcurrentHashMap.newKeySet();
    private final Random random = new Random();

    @Autowired
    public PaymentService(KafkaTemplate<String, KafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "payment-requests")
    public void processPayment(PaymentRequest request) {
        String paymentKey = request.getUuid();

        if (processedPayments.contains(paymentKey)) {
            log.warn("중복 결제 요청 감지: 주문 ID {}", paymentKey);
            return;
        }

        try {
            processedPayments.add(paymentKey);

            // 실제 결제 로직을 여기에 구현합니다.
            // 현재는 시뮬레이션을 위해 90% 확률로 성공으로 처리합니다.
            boolean isSuccess = random.nextInt(100) < 90;

            PaymentResponse result = new PaymentResponse(request.getUserId(), paymentKey, isSuccess);

            if (isSuccess) {
                log.info("결제 성공: 주문 ID {}", paymentKey);
            } else {
                log.info("결제 실패: 주문 ID {}", paymentKey);
            }

            // 결제 결과를 Kafka로 전송
            kafkaTemplate.send("payment-results", result);

        } catch (Exception e) {
            log.error("결제 처리 중 오류 발생: 주문 ID {}", paymentKey, e);
            PaymentResponse errorResult = new PaymentResponse(request.getUserId(), paymentKey, false);
            kafkaTemplate.send("payment-results", errorResult);
        } finally {
            // 일정 시간 후에 처리된 결제 목록에서 제거 (메모리 관리를 위해)
            scheduleRemovalFromProcessedPayments(paymentKey);
        }
    }

    private void scheduleRemovalFromProcessedPayments(String paymentKey) {
        // 예: 30분 후에 처리된 결제 목록에서 제거
        // 실제 구현 시 스케줄러나 캐시 라이브러리를 사용할 수 있습니다.
        new Thread(() -> {
            try {
                Thread.sleep(30 * 60 * 1000); // 30분
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            processedPayments.remove(paymentKey);
        }).start();
    }

    public void refundPayment(Order order) {
        // 실제 환불 처리 대신 로그만 남김
        log.info("환불 처리 완료: 주문 ID {}, 금액 {}", order.getId(), order.getPaymentAmount());

        // 환불 결과를 Kafka로 전송
        RefundResponse refundResult = new RefundResponse(order.getUserId(), order.getId(), true, order.getStatus());
        kafkaTemplate.send("refund-results", refundResult);
    }
}