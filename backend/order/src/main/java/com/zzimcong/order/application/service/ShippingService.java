package com.zzimcong.order.application.service;

import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderStatus;
import com.zzimcong.order.domain.repository.OrderRepository;
import com.zzimcong.zzimconginventorycore.common.event.OrderEvent;
import com.zzimcong.zzimconginventorycore.common.event.OrderEventType;
import com.zzimcong.zzimconginventorycore.common.model.KafkaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableScheduling
public class ShippingService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Autowired
    public ShippingService(OrderRepository orderRepository, KafkaTemplate<String, KafkaMessage> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 60000) // 1분마다 실행
    @Transactional
    public void updateOrdersToPreparing() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<Order> orders = orderRepository.findByStatusAndCreatedAtBefore(OrderStatus.ORDER_COMPLETED, oneHourAgo);

        for (Order order : orders) {
            order.setStatus(OrderStatus.PREPARING_FOR_SHIPMENT);
            orderRepository.save(order);
            kafkaTemplate.send("order-events", new OrderEvent(order.getId(), OrderEventType.SHIPMENT_PREPARING));
        }
    }

    @Scheduled(cron = "0 0 23 * * ?") // 매일 오후 11시 실행
    @Transactional
    public void updateOrdersToShipping() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.PREPARING_FOR_SHIPMENT);

        for (Order order : orders) {
            order.setStatus(OrderStatus.SHIPPING);
            orderRepository.save(order);
            kafkaTemplate.send("order-events", new OrderEvent(order.getId(), OrderEventType.SHIPMENT_STARTED));
        }
    }

    @Scheduled(cron = "0 0 11 * * ?") // 매일 오전 11시 실행
    @Transactional
    public void updateOrdersToDelivered() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.SHIPPING);

        for (Order order : orders) {
            order.setStatus(OrderStatus.DELIVERED);
            orderRepository.save(order);
            kafkaTemplate.send("order-events", new OrderEvent(order.getId(), OrderEventType.SHIPMENT_DELIVERED));
        }
    }

    public void startShipping(Order order) {
        order.setStatus(OrderStatus.SHIPPING);
        orderRepository.save(order);
    }

    public void completeDelivery(Order order) {
        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
    }
}