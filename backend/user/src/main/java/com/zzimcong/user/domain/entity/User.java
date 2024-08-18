package com.zzimcong.user.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String email;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 64)
    private String phone;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean abuser = false;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean signout = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @ColumnDefault("'USER'")
    private UserRole role = UserRole.USER;
}
