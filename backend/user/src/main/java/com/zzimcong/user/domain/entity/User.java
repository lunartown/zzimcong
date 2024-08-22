package com.zzimcong.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
    @Builder.Default
    private Boolean abuser = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean signout = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private UserRole role = UserRole.USER;

    public static UserRole defaultRole(UserRole role) {
        return role != null ? role : UserRole.USER;
    }

    public void signOut() {
        this.signout = true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // 비밀번호 변경 메서드
    public void setPassword(String newPassword) {
        // 여기에 비밀번호 유효성 검사 로직을 추가할 수 있습니다.
        this.password = newPassword;
    }

    // role을 변경하는 메서드
    public void changeRole(UserRole newRole) {
        this.role = newRole != null ? newRole : UserRole.USER;
    }
}