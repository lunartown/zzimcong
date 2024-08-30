package com.zzimcong.order.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "payment_details")
public class PaymentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_details_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentType paymentMethod;

    private String cardNumber;
    private String cardHolderName;
    private String expirationDate;
    private String cvv;

    @OneToOne(mappedBy = "paymentDetails")
    private Order order;
}
