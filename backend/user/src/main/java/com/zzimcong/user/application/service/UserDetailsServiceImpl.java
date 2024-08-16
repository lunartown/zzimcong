package com.zzimcong.user.application.service;

import com.zzimcong.user.common.util.AESUtil;
import com.zzimcong.user.domain.entity.User;
import com.zzimcong.user.common.exception.NotFoundException;
import com.zzimcong.user.common.exception.ErrorCode;
import com.zzimcong.user.domain.repository.UserRepository;
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
                .withUsername(String.valueOf(user.getId()))
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}