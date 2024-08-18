package com.zzimcong.order.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzimcong.order.OrderApplication;
import com.zzimcong.order.application.dto.OrderRequest;
import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {OrderApplication.class})
@AutoConfigureMockMvc
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void createOrder_Integration() throws Exception {
        // Given
        OrderRequest orderRequest = new OrderRequest();
        // orderRequest 설정...

        // When
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isAccepted());

        // Then
        // 주문이 데이터베이스에 생성되었는지 확인
        List<Order> createdOrders = orderRepository.findAll(); // 모든 주문을 가져옵니다
        assertFalse(createdOrders.isEmpty()); // 주문 목록이 비어있지 않은지 확인

        Order lastCreatedOrder = createdOrders.get(createdOrders.size() - 1); // 마지막으로 생성된 주문
        assertEquals(orderRequest.getName(), lastCreatedOrder.getName()); // 이름 확인
        assertEquals(orderRequest.getOrderAmount(), lastCreatedOrder.getOrderAmount()); // 주문 금액 확인
    }
}