package com.zzimcong.order.domain.entity;

import com.zzimcong.order.application.dto.OrderRequest;
import com.zzimcong.order.application.dto.OrderRequest.PaymentType;
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
    public Long id;

    @Column(nullable = false)
    public Long userId;

    @Column(nullable = false)
    public BigDecimal orderAmount;

    @Column(nullable = false)
    public BigDecimal paymentAmount;

    public PaymentType payment;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'PENDING'")
    public OrderState state;

    public boolean deleted = false;

    public String reason;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String addr;

    @Column(nullable = false)
    public String addrDetail;

    @Column(nullable = false)
    public String zipcode;

    @Column(nullable = false)
    public String phone;

    public String message;

    public LocalDateTime deliveredAt;

    public LocalDateTime refundRequestedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<OrderItem> orderItems = new ArrayList<>();

    private Order(Long userId, OrderRequest orderRequest) {
        this.userId = userId;
        this.orderAmount = orderRequest.getOrderAmount();
        this.paymentAmount = orderRequest.getPaymentAmount();
        this.payment = orderRequest.getPayment();
        this.name = orderRequest.getName();
        this.addr = orderRequest.getAddr();
        this.addrDetail = orderRequest.getAddrDetail();
        this.zipcode = orderRequest.getZipcode();
        this.phone = orderRequest.getPhone();
        this.message = orderRequest.getMessage();
    }

    //factory method
    public static Order createOrder(Long userId, OrderRequest orderRequest) {
        return new Order(userId, orderRequest);
    }

    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
        item.setOrder(this);
    }
}
