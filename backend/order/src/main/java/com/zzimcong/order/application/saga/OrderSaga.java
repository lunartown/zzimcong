package com.zzimcong.order.application.saga;

import com.zzimcong.order.application.dto.OrderRequest;
import com.zzimcong.order.application.dto.PaymentRequest;
import com.zzimcong.order.application.dto.PaymentResult;
import com.zzimcong.order.application.service.OrderService;
import com.zzimcong.order.application.service.PaymentService;
import com.zzimcong.order.application.service.ProductService;
import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderStatus;
import com.zzimcong.zzimconginventorycore.common.event.OrderEvent;
import com.zzimcong.zzimconginventorycore.common.event.OrderEventType;
import com.zzimcong.zzimconginventorycore.common.model.KafkaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderSaga {
    private final OrderService orderService;
    private final ProductService productService;
    private final PaymentService paymentService;
    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Autowired
    public OrderSaga(OrderService orderService, ProductService productService,
                     PaymentService paymentService,
                     KafkaTemplate<String, KafkaMessage> kafkaTemplate) {
        this.orderService = orderService;
        this.productService = productService;
        this.paymentService = paymentService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void startOrderSaga(OrderRequest request) {
        Order order = orderService.createOrder(request);
        kafkaTemplate.send("order-events", new OrderEvent(order.getId(), OrderEventType.ORDER_CREATED));
    }

    @KafkaListener(topics = "order-events")
    public void handleOrderEvent(OrderEvent event) {
        Order order = orderService.getOrder(event.getOrderId());
        switch (event.getEventType()) {
            case ORDER_CREATED:
                reserveInventory(order);
                break;
            case INVENTORY_RESERVED:
                processPayment(order);
                break;
            case PAYMENT_PROCESSED:
                completeOrder(order);
                break;
            case SHIPMENT_PREPARING:
            case SHIPMENT_STARTED:
            case SHIPMENT_DELIVERED:
                orderService.updateOrderStatus(order, mapEventTypeToOrderStatus(event.getEventType()));
                break;
            case SAGA_FAILED:
                compensateOrderFailure(order, event.getErrorMessage());
                break;
        }
    }

    private void reserveInventory(Order order) {
        boolean allReserved = order.getOrderItems().stream()
                .allMatch(item -> productService.reserveInventory(item.getProductId(), item.getQuantity()));

        if (allReserved) {
            kafkaTemplate.send("order-events", new OrderEvent(order.getId(), OrderEventType.INVENTORY_RESERVED));
        } else {
            kafkaTemplate.send("order-events", new OrderEvent(order.getId(), OrderEventType.SAGA_FAILED, "재고 예약 실패"));
        }
    }

    private void processPayment(Order order) {
        // Kafka를 통해 결제 서비스에 비동기 요청
        kafkaTemplate.send("payment-requests", new PaymentRequest(order.getId(), order.getPaymentAmount()));
    }

    @KafkaListener(topics = "payment-results")
    public void handlePaymentResult(PaymentResult result) {
        if (result.isSuccess()) {
            kafkaTemplate.send("order-events", new OrderEvent(result.getOrderId(), OrderEventType.PAYMENT_PROCESSED));
        } else {
            kafkaTemplate.send("order-events", new OrderEvent(result.getOrderId(), OrderEventType.SAGA_FAILED, "결제 실패"));
        }
    }

    private void completeOrder(Order order) {
        orderService.completeOrder(order);
        kafkaTemplate.send("order-events", new OrderEvent(order.getId(), OrderEventType.ORDER_COMPLETED));
    }

    private void compensateOrderFailure(Order order, String errorMessage) {
        // 실패 원인에 따라 보상 트랜잭션 수행
        if (errorMessage.contains("재고 예약 실패")) {
            orderService.cancelOrder(order);
        } else if (errorMessage.contains("결제 처리 실패")) {
            order.getOrderItems()
                    .forEach(item -> productService.releaseInventory(item.getProductId(), item.getQuantity()));
            orderService.cancelOrder(order);
        } else if (errorMessage.contains("배송 준비 실패")) {
            paymentService.refundPayment(order);
            order.getOrderItems()
                    .forEach(item -> productService.releaseInventory(item.getProductId(), item.getQuantity()));
            orderService.cancelOrder(order);
        }
    }

    private OrderStatus mapEventTypeToOrderStatus(OrderEventType eventType) {
        return switch (eventType) {
            case SHIPMENT_PREPARING -> OrderStatus.PREPARING_FOR_SHIPMENT;
            case SHIPMENT_STARTED -> OrderStatus.SHIPPING;
            case SHIPMENT_DELIVERED -> OrderStatus.DELIVERED;
            default -> throw new IllegalArgumentException("Unexpected event type: " + eventType);
        };
    }
}