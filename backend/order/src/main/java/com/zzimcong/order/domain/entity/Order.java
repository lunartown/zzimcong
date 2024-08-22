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
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_address_id")
    private OrderAddressRequest orderAddressRequest;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal orderAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal paymentAmount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_details_id")
    private PaymentDetails paymentDetails;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    @ColumnDefault("'ORDER_COMPLETED'")
    private OrderStatus status = OrderStatus.ORDER_COMPLETED;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(length = 255)
    private String cancellationReason;

    @Column(length = 255)
    private String refundReason;

    private LocalDateTime deliveredAt;

    private LocalDateTime refundRequestedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
}