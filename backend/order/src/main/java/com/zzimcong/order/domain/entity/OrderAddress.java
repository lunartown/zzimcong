package com.zzimcong.order.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_addresses")
public class OrderAddress extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_address_id")
    private Long id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false, length = 255)
    private String streetAddress;

    @Column(nullable = false, length = 255)
    private String addressDetail;

    @Column(nullable = false, length = 10)
    private String zipcode;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(length = 255)
    private String message;

    @OneToOne(mappedBy = "orderAddress")
    private Order order;
}