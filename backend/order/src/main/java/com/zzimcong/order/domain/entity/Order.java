package com.zzimcong.order.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders") // 예약어 회피를 위해 복수형 사용
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal orderAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentType payment;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    @ColumnDefault("'CREATED'")
    private OrderStatus status = OrderStatus.CREATED;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(length = 255)
    private String reason;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false, length = 255)
    private String addr;

    @Column(nullable = false, length = 255)
    private String addrDetail;

    @Column(nullable = false, length = 10)
    private String zipcode;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(length = 255)
    private String message;

    private LocalDateTime deliveredAt;

    private LocalDateTime refundRequestedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
}