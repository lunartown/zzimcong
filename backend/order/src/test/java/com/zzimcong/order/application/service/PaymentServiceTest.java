//package com.zzimcong.order.application.service;
//
//import com.zzimcong.order.application.dto.PaymentRequest;
//import com.zzimcong.order.application.dto.PaymentResponse;
//import com.zzimcong.order.application.dto.RefundResponse;
//import com.zzimcong.order.domain.entity.Order;
//import com.zzimcong.order.domain.entity.OrderStatus;
//import com.zzimcong.zzimconginventorycore.common.model.KafkaMessage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.kafka.core.KafkaTemplate;
//
//import java.math.BigDecimal;
//
//import static org.mockito.Mockito.*;
//
//class PaymentServiceTest {
//
//    @Mock
//    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;
//
//    @InjectMocks
//    private PaymentService paymentService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testProcessPayment() {
//        PaymentRequest request = new PaymentRequest(1L, "uuid", null, BigDecimal.TEN);
//
//        paymentService.processPayment(request);
//
//        verify(kafkaTemplate).send(eq("payment-results"), any(PaymentResponse.class));
//    }
//
//    @Test
//    void testRefundPayment() {
//        Order order = new Order();
//        order.setId(1L);
//        order.setUserId(1L);
//        order.setPaymentAmount(BigDecimal.TEN);
//        order.setStatus(OrderStatus.REFUND_REQUESTED);
//
//        paymentService.refundPayment(order);
//
//        verify(kafkaTemplate).send(eq("refund-results"), any(RefundResponse.class));
//    }
//}