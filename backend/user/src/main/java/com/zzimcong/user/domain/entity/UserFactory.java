package com.zzimcong.user.domain.entity;

import com.zzimcong.user.application.dto.SignupRequest;
import com.zzimcong.user.common.util.AESUtil;
import com.zzimcong.user.domain.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// UserFactory.java
@Component
public class UserFactory {
    private final UserMapper userMapper;
    private final AESUtil aesUtil;
    private final PasswordEncoder passwordEncoder;

    public UserFactory(UserMapper userMapper, AESUtil aesUtil, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.aesUtil = aesUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignupRequest dto) {
        return userMapper.toEntity(dto);
    }

    public User createAdminUser(SignupRequest dto) {
        User user = userMapper.toEntity(dto);
        user.changeRole(UserRole.ADMIN);
        return user;
    }

    public User createSellerUser(SignupRequest dto) {
        User user = userMapper.toEntity(dto);
        user.changeRole(UserRole.SELLER);
        return user;
    }
}