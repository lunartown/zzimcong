package com.lunartown.zzimcong.user.service;

import com.lunartown.zzimcong.common.util.AESUtil;
import com.lunartown.zzimcong.user.entity.User;
import com.lunartown.zzimcong.user.exception.ErrorCode;
import com.lunartown.zzimcong.user.exception.NotFoundException;
import com.lunartown.zzimcong.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final AESUtil aesUtil;

    public UserDetailsServiceImpl(UserRepository userRepository, AESUtil aesUtil) {
        this.userRepository = userRepository;
        this.aesUtil = aesUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(aesUtil.encrypt(email))
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}