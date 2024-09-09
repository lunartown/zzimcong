package com.zzimcong.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "address")
@Getter
@Setter
@Builder
@SQLDelete(sql = "UPDATE address SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @Column(name = "address_detail", nullable = false)
    private String addressDetail;

    @Column(name = "zipcode", nullable = false, length = 10)
    private String zipcode;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "message")
    private String message;

    @Column(name = "is_default")
    private Boolean isDefault = false;
}