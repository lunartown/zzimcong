//package com.zzimcong.order.application.saga;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zzimcong.order.application.dto.*;
//import com.zzimcong.order.application.mapper.OrderRequestMapper;
//import com.zzimcong.order.application.service.ProductService;
//import com.zzimcong.order.common.exception.InternalServerErrorException;
//import com.zzimcong.order.domain.entity.Order;
//import com.zzimcong.order.domain.entity.OrderStatus;
//import com.zzimcong.order.domain.repository.OrderRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.kafka.core.KafkaTemplate;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//import java.util.concurrent.CompletableFuture;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class OrderSagaTest {
//
//    @Mock
//    private ProductService productService;
//    @Mock
//    private OrderRequestMapper orderRequestMapper;
//    @Mock
//    private ObjectMapper objectMapper;
//    @Mock
//    private RedisTemplate<String, String> redisTemplate;
//    @Mock
//    private OrderRepository orderRepository;
//    @Mock
//    private KafkaTemplate<String, PaymentRequest> kafkaTemplate;
//    @Mock
//    private ValueOperations<String, String> valueOperations;
//
//    @InjectMocks
//    private OrderSaga orderSaga;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
//    }
//
//    @Test
//    void testPrepareOrder() throws Exception {
//        OrderPreparationRequest request = new OrderPreparationRequest(
//                BigDecimal.TEN, BigDecimal.TEN,
//                Collections.singletonList(new OrderItemRequest(1L, BigDecimal.ONE, 1))
//        );
//
//        when(productService.reserveInventory(anyLong(), anyInt())).thenReturn(true);
//        when(orderRequestMapper.orderPreperationRequestToOrder(any())).thenReturn(new Order());
//        when(objectMapper.writeValueAsString(any())).thenReturn("{}");
//        when(valueOperations.setIfAbsent(anyString(), anyString(), any())).thenReturn(true);
//
//        OrderPreparationResponse response = orderSaga.prepareOrder(1L, request);
//
//        assertNotNull(response);
//        assertEquals(OrderPreparationStatus.SUCCESS, response.status());
//        verify(productService).reserveInventory(anyLong(), anyInt());
//        verify(orderRequestMapper).orderPreperationRequestToOrder(any());
//        verify(objectMapper).writeValueAsString(any());
//        verify(valueOperations).setIfAbsent(anyString(), anyString(), any());
//    }
//
//    @Test
//    void testCreateOrder() throws Exception {
//        OrderCreationRequest request = new OrderCreationRequest(
//                new OrderAddressRequest("name", "street", "detail", "12345", "1234567890", "message"),
//                new PaymentDetailsRequest(null, "1234", "John Doe", "12/24", "123")
//        );
//
//        Order order = new Order();
//        order.setPaymentAmount(BigDecimal.TEN);
//
//        when(valueOperations.get(anyString())).thenReturn("{}");
//        when(objectMapper.readValue(anyString(), eq(Order.class))).thenReturn(order);
//        when(kafkaTemplate.send(anyString(), any(PaymentRequest.class))).thenReturn(mock(CompletableFuture.class));
//
//        assertDoesNotThrow(() -> orderSaga.createOrder(1L, "uuid", request));
//
//        verify(valueOperations).get(anyString());
//        verify(objectMapper).readValue(anyString(), eq(Order.class));
//        verify(orderRequestMapper).updateOrderFromRequest(any(), any());
//        verify(kafkaTemplate).send(anyString(), any(PaymentRequest.class));
//    }
//
//    @Test
//    void testHandlePaymentResult() throws Exception {
//        PaymentResponse result = new PaymentResponse(1L, "uuid", true);
//        Order order = new Order();
//
//        when(valueOperations.get(anyString())).thenReturn("{}");
//        when(objectMapper.readValue(anyString(), eq(Order.class))).thenReturn(order);
//        when(orderRepository.save(any(Order.class))).thenReturn(order);
//
//        orderSaga.handlePaymentResult(result);
//
//        verify(valueOperations).get(anyString());
//        verify(objectMapper).readValue(anyString(), eq(Order.class));
//        verify(orderRepository).save(any(Order.class));
//        verify(redisTemplate).delete(anyString());
//    }
//
//    @Test
//    void testProcessSuccessfulPayment() {
//        Order order = new Order();
//        when(orderRepository.save(any(Order.class))).thenReturn(order);
//
//        assertDoesNotThrow(() -> orderSaga.processSuccessfulPayment(order));
//
//        assertEquals(OrderStatus.ORDER_COMPLETED, order.getStatus());
//        verify(orderRepository).save(order);
//    }
//
//    @Test
//    void testProcessSuccessfulPaymentFailed() {
//        Order order = new Order();
//        when(orderRepository.save(any(Order.class))).thenThrow(new RuntimeException("DB Error"));
//
//        assertThrows(InternalServerErrorException.class, () -> orderSaga.processSuccessfulPayment(order));
//    }
//}