package com.zzimcong.order.domain.repository;

import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus orderStatus);

    List<Order> findByStatusAndDeliveredAtBefore(OrderStatus orderStatus, LocalDateTime confirmationDate);

    List<Order> findByStatusAndCreatedAtBefore(OrderStatus orderStatus, LocalDateTime oneHourAgo);

    Page<Order> findByUserId(Long userId, Pageable pageable);
}
