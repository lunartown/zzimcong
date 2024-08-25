//package com.zzimcong.order.application.service;
//
//import com.zzimcong.order.application.dto.OrderResponse;
//import com.zzimcong.order.application.mapper.OrderMapper;
//import com.zzimcong.order.common.exception.NotFoundException;
//import com.zzimcong.order.domain.entity.Order;
//import com.zzimcong.order.domain.entity.OrderStatus;
//import com.zzimcong.order.domain.repository.OrderRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.kafka.core.KafkaTemplate;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//class OrderServiceTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private OrderMapper orderMapper;
//
//    @Mock
//    private KafkaTemplate<String, Order> kafkaTemplate;
//
//    @InjectMocks
//    private OrderService orderService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetOrder() {
//        Order order = new Order();
//        order.setId(1L);
//
//        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
//
//        Order result = orderService.getOrder(1L);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//
//        verify(orderRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testGetOrderNotFound() {
//        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> orderService.getOrder(1L));
//
//        verify(orderRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testGetOrderResponse() {
//        Order order = new Order();
//        order.setId(1L);
//
//        OrderResponse orderResponse = new OrderResponse(1L, 1L, null, null, null, null, false, null, null, null);
//
//        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
//        when(orderMapper.orderToOrderResponse(order)).thenReturn(orderResponse);
//
//        OrderResponse result = orderService.getOrderResponse(1L);
//
//        assertNotNull(result);
//        assertEquals(1L, result.id());
//
//        verify(orderRepository, times(1)).findById(1L);
//        verify(orderMapper, times(1)).orderToOrderResponse(order);
//    }
//
//    @Test
//    void testGetOrderList() {
//        Order order1 = new Order();
//        Order order2 = new Order();
//        List<Order> orders = Arrays.asList(order1, order2);
//        Page<Order> orderPage = new PageImpl<>(orders);
//
//        OrderResponse orderResponse1 = new OrderResponse(1L, 1L, null, null, null, null, false, null, null, null);
//        OrderResponse orderResponse2 = new OrderResponse(2L, 1L, null, null, null, null, false, null, null, null);
//
//        when(orderRepository.findAll(any(PageRequest.class))).thenReturn(orderPage);
//        when(orderMapper.orderToOrderResponse(order1)).thenReturn(orderResponse1);
//        when(orderMapper.orderToOrderResponse(order2)).thenReturn(orderResponse2);
//
//        Page<OrderResponse> result = orderService.getOrderList(PageRequest.of(0, 10));
//
//        assertNotNull(result);
//        assertEquals(2, result.getContent().size());
//
//        verify(orderRepository, times(1)).findAll(any(PageRequest.class));
//        verify(orderMapper, times(2)).orderToOrderResponse(any(Order.class));
//    }
//
//    @Test
//    void testGetUserOrders() {
//        Order order1 = new Order();
//        Order order2 = new Order();
//        List<Order> orders = Arrays.asList(order1, order2);
//        Page<Order> orderPage = new PageImpl<>(orders);
//
//        OrderResponse orderResponse1 = new OrderResponse(1L, 1L, null, null, null, null, false, null, null, null);
//        OrderResponse orderResponse2 = new OrderResponse(2L, 1L, null, null, null, null, false, null, null, null);
//
//        when(orderRepository.findByUserId(eq(1L), any(PageRequest.class))).thenReturn(orderPage);
//        when(orderMapper.orderToOrderResponse(order1)).thenReturn(orderResponse1);
//        when(orderMapper.orderToOrderResponse(order2)).thenReturn(orderResponse2);
//
//        Page<OrderResponse> result = orderService.getUserOrders(1L, PageRequest.of(0, 10));
//
//        assertNotNull(result);
//        assertEquals(2, result.getContent().size());
//
//        verify(orderRepository, times(1)).findByUserId(eq(1L), any(PageRequest.class));
//        verify(orderMapper, times(2)).orderToOrderResponse(any(Order.class));
//    }
//
//    @Test
//    void testUpdateOrderStatus() {
//        Order order = new Order();
//        order.setId(1L);
//        order.setStatus(OrderStatus.ORDER_COMPLETED);
//
//        when(orderRepository.save(any(Order.class))).thenReturn(order);
//
//        orderService.updateOrderStatus(order, OrderStatus.SHIPPING);
//
//        assertEquals(OrderStatus.SHIPPING, order.getStatus());
//
//        verify(orderRepository, times(1)).save(order);
//    }
//
//    @Test
//    void testCancelOrderByOrderId() {
//        Order order = new Order();
//        order.setId(1L);
//        order.setStatus(OrderStatus.ORDER_COMPLETED);
//
//        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
//
//        orderService.cancelOrderByOrderId(1L);
//
//        verify(orderRepository, times(1)).findById(1L);
//        verify(kafkaTemplate, times(1)).send(eq("refund-request"), eq(order));
//    }
//
//    @Test
//    void testRequestRefund() {
//        Order order = new Order();
//        order.setId(1L);
//        order.setStatus(OrderStatus.DELIVERED);
//
//        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
//
//        orderService.requestRefund(1L);
//
//        assertEquals(OrderStatus.REFUND_REQUESTED, order.getStatus());
//
//        verify(orderRepository, times(1)).findById(1L);
//        verify(orderRepository, times(1)).save(order);
//    }
//
//    @Test
//    void testIsOrderOwnedByUser() {
//        Order order = new Order();
//        order.setId(1L);
//        order.setUserId(1L);
//
//        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
//
//        boolean result = orderService.isOrderOwnedByUser(1L, 1L);
//
//        assertTrue(result);
//
//        verify(orderRepository, times(1)).findById(1L);
//    }
//}