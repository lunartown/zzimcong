package com.lunartown.zzimcong.order.entity;

import com.lunartown.zzimcong.product.entity.Product;
import com.lunartown.zzimcong.user.entity.BaseEntity;
import com.lunartown.zzimcong.user.entity.User;
import jakarta.persistence.*;

@Entity
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    public Long id;

    @Column(nullable = false)
    public Long orderAmount;

    @Column(nullable = false)
    public Long paymentAmount;

    @Column(name = "payment")
    public String payment;

    @Column(name = "status")
    public String status;

    @Column(name = "deleted")
    public String deleted;

    @Column(name = "reason")
    public String reason;

    @Column(name = "name")
    public String name;

    @Column(name = "addr")
    public String addr;

    @Column(name = "addrDetail")
    public String addrDetail;

    @Column(name = "zipcode")
    public String zipcode;

    @Column(name = "phone")
    public String phone;

    @Column(name = "message")
    public String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    public Product product;

    
}
