package com.zzimcong.order.application.service;

import com.zzimcong.order.application.dto.PaymentRequest;
import com.zzimcong.order.application.dto.PaymentResponse;
import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.zzimconginventorycore.common.model.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j(topic = "payment-service")
@Service
public class PaymentService {
    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;
    private final Random random = new Random();

    @Autowired
    public PaymentService(KafkaTemplate<String, KafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "payment-requests")
    public void processPayment(PaymentRequest request) {
        // 90% 확률로 결제 성공, 10% 확률로 결제 실패 시뮬레이션
        boolean isSuccess = random.nextInt(100) < 90;

        PaymentResponse result = new PaymentResponse(request.getUserId(), request.getUuid(), isSuccess);

        if (isSuccess) {
            log.info("결제 성공: 주문 ID {}", request.getUuid());
        } else {
            log.info("결제 실패: 주문 ID {}", request.getUuid());
        }

        // 결제 결과를 Kafka로 전송
        kafkaTemplate.send("payment-results", result);
    }

    public void refundPayment(Order order) {
        // 실제 환불 처리 대신 로그만 남김
        log.info("환불 처리 완료: 주문 ID {}, 금액 {}", order.getId(), order.getPaymentAmount());

        // 환불 결과를 Kafka로 전송
        PaymentResponse refundResult = new PaymentResponse(order.getUserId(), order.getId(), true, "REFUNDED");
        kafkaTemplate.send("payment-results", refundResult);
    }
}