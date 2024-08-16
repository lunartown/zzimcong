package com.zzimcong.order.domain.repository;

import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndUserId(Long orderId, Long userId);

    List<Order> findByStateAndDeliveredAtBefore(OrderState orderState, LocalDateTime oneDayAgo);

    List<Order> findByStateAndRefundRequestedAtBefore(OrderState orderState, LocalDateTime oneDayAgo);

    Optional<Order> findByProductIdAndQuantity(Long productId, int quantity);
}
