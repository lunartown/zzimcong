//package com.zzimcong.order.application.service;
//
//import com.zzimcong.order.domain.entity.Order;
//import com.zzimcong.order.domain.entity.OrderStatus;
//import com.zzimcong.order.domain.repository.OrderRepository;
//import com.zzimcong.zzimconginventorycore.common.event.ShippingEvent;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.kafka.core.KafkaTemplate;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//
//class ShippingServiceTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private KafkaTemplate<String, ShippingEvent> kafkaTemplate;
//
//    @InjectMocks
//    private ShippingService shippingService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testUpdateOrdersToPreparing() {
//        Order order1 = new Order();
//        order1.setId(1L);
//        Order order2 = new Order();
//        order2.setId(2L);
//        List<Order> orders = Arrays.asList(order1, order2);
//
//        when(orderRepository.findByStatusAndCreatedAtBefore(eq(OrderStatus.ORDER_COMPLETED), any(LocalDateTime.class)))
//                .thenReturn(orders);
//
//        shippingService.updateOrdersToPreparing();
//
//        verify(kafkaTemplate, times(2)).send(eq("shipping-events"), any(ShippingEvent.class));
//    }
//
//    @Test
//    void testUpdateOrdersToShipping() {
//        Order order1 = new Order();
//        order1.setId(1L);
//        Order order2 = new Order();
//        order2.setId(2L);
//        List<Order> orders = Arrays.asList(order1, order2);
//
//        when(orderRepository.findByStatus(OrderStatus.PREPARING_FOR_SHIPMENT)).thenReturn(orders);
//
//        shippingService.updateOrdersToShipping();
//
//        verify(kafkaTemplate, times(2)).send(eq("shipping-events"), any(ShippingEvent.class));
//    }
//
//    @Test
//    void testUpdateOrdersToDelivered() {
//        Order order1 = new Order();
//        order1.setId(1L);
//        Order order2 = new Order();
//        order2.setId(2L);
//        List<Order> orders = Arrays.asList(order1, order2);
//
//        when(orderRepository.findByStatus(OrderStatus.SHIPPING)).thenReturn(orders);
//
//        shippingService.updateOrdersToDelivered();
//
//        verify(kafkaTemplate, times(2)).send(eq("shipping-events"), any(ShippingEvent.class));
//    }
//
//    @Test
//    void testAutoRefundOrders() {
//        Order order1 = new Order();
//        order1.setId(1L);
//        Order order2 = new Order();
//        order2.setId(2L);
//        List<Order> orders = Arrays.asList(order1, order2);
//
//        when(orderRepository.findByStatusAndDeliveredAtBefore(eq(OrderStatus.REFUND_REQUESTED), any(LocalDateTime.class)))
//                .thenReturn(orders);
//
//        shippingService.autoRefundOrders();
//
//        verify(kafkaTemplate, times(2)).send(eq("shipping-events"), any(ShippingEvent.class));
//    }
//}