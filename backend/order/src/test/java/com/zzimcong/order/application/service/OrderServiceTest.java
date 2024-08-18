package com.zzimcong.order.application.service;

import com.zzimcong.order.application.dto.OrderItemRequest;
import com.zzimcong.order.application.dto.OrderRequest;
import com.zzimcong.order.common.exception.BadRequestException;
import com.zzimcong.order.common.exception.NotFoundException;
import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderStatus;
import com.zzimcong.order.domain.entity.PaymentType;
import com.zzimcong.order.domain.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //createOrder
    @Test
    void createOrder_Success() {
        // Given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderAmount(new BigDecimal("100.00"));
        orderRequest.setPaymentAmount(new BigDecimal("100.00"));
        orderRequest.setPayment(PaymentType.KB);
        orderRequest.setName("John Doe");
        orderRequest.setAddr("123 Test St");
        orderRequest.setAddrDetail("Apt 4");
        orderRequest.setZipcode("12345");
        orderRequest.setPhone("123-456-7890");

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setPrice(100);
        itemRequest.setQuantity(1);
        orderRequest.setItems(Arrays.asList(itemRequest));

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // When
        Order result = orderService.createOrder(orderRequest);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_InvalidInput_ThrowsException() {
        // Given
        OrderRequest orderRequest = new OrderRequest();
        // Set invalid input (e.g., negative amount)
        orderRequest.setOrderAmount(new BigDecimal("-100.00"));

        // When & Then
        assertThrows(BadRequestException.class, () -> {
            orderService.createOrder(orderRequest);
        });

        // Verify that save was not called
        verify(orderRepository, never()).save(any(Order.class));
    }

    //getOrder
    @Test
    void getOrder_ExistingOrder_ReturnsOrder() {
        // Given
        Long orderId = 1L;
        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        // When
        Order result = orderService.getOrder(orderId);

        // Then
        assertNotNull(result);
        assertEquals(orderId, result.getId());
    }

    @Test
    void getOrder_NonExistingOrder_ThrowsException() {
        // Given
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            orderService.getOrder(orderId);
        });
    }

    //cancelOrderByOrderId
    @Test
    void cancelOrder_SuccessfulCancellation() {
        // Given
        Long orderId = 1L;
        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setStatus(OrderStatus.ORDER_COMPLETED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        // When
        orderService.cancelOrderByOrderId(orderId);

        // Then
        verify(orderRepository, times(1)).save(argThat(order ->
                order.getStatus() == OrderStatus.CANCELED
        ));
        verify(paymentService, times(1)).refundPayment(mockOrder);
    }
}