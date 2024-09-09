package com.zzimcong.order.application.service;

import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderStatus;
import com.zzimcong.order.domain.repository.OrderRepository;
import com.zzimcong.zzimconginventorycore.common.event.ShippingEvent;
import com.zzimcong.zzimconginventorycore.common.event.ShippingEventType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j(topic = "shipping-service")
@Service
@EnableScheduling
@AllArgsConstructor
public class ShippingService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, ShippingEvent> kafkaTemplate;
//    private final JavaMailSender javaMailSender;

    // 주문 완료 -> 배송 준비로 상태 변경
    // 1시간 전에 주문 완료된 주문을 찾아 배송 준비로 변경
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void updateOrdersToPreparing() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<Order> orders = orderRepository.findByStatusAndCreatedAtBefore(OrderStatus.ORDER_COMPLETED, oneHourAgo);

        for (Order order : orders) {
//            order.setStatus(OrderStatus.PREPARING_FOR_SHIPMENT);
//            orderRepository.save(order);
            kafkaTemplate.send("shipping-events", new ShippingEvent(order.getId(), ShippingEventType.PREPARING));
        }
    }

    // 배송 준비 -> 배송 중으로 상태 변경
    // 매일 오후 11시 실행
    @Scheduled(cron = "0 0 23 * * ?")
    @Transactional
    public void updateOrdersToShipping() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.PREPARING_FOR_SHIPMENT);

        for (Order order : orders) {
//            order.setStatus(OrderStatus.SHIPPING);
//            orderRepository.save(order);
            kafkaTemplate.send("shipping-events", new ShippingEvent(order.getId(), ShippingEventType.SHIPPING));
        }
    }

    // 배송 중 -> 배송 완료로 상태 변경
    // 매일 오전 11시 실행
    @Scheduled(cron = "0 0 11 * * ?")
    @Transactional
    public void updateOrdersToDelivered() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.SHIPPING);

        for (Order order : orders) {
//            order.setStatus(OrderStatus.DELIVERED);
//            orderRepository.save(order);
            kafkaTemplate.send("shipping-events", new ShippingEvent(order.getId(), ShippingEventType.DELIVERED));
//            sendMail(order);
        }
    }

    // 반품 요청 -> 환불 완료로 상태 변경
    @Scheduled(fixedDelay = 86400000)
    public void autoRefundOrders() {
        log.info("Starting auto-refund of orders");
        LocalDateTime refundDate = LocalDateTime.now().minusDays(1);
        List<Order> ordersToRefund =
                orderRepository.findByStatusAndDeliveredAtBefore(OrderStatus.REFUND_REQUESTED, refundDate);
        log.info("Found {} orders to auto-refund", ordersToRefund.size());

        for (Order order : ordersToRefund) {
//            order.setStatus(OrderStatus.REFUND_COMPLETED);
//            orderRepository.save(order);
            kafkaTemplate.send("shipping-events", new ShippingEvent(order.getId(), ShippingEventType.PICKUP_COMPLETED));
        }
    }

    // 메일 전송
//    private void sendMail(Order order) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(order.getUserId().getEmail());
//        message.setSubject("배송이 완료되었습니다.");
//        message.setText("배송이 완료되었습니다. 주문 번호: " + order.getId());
//        javaMailSender.send(message);
//    }
}