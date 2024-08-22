package com.zzimcong.user.application.service;

import com.zzimcong.user.application.dto.SignupRequest;
import com.zzimcong.user.application.dto.UserModifyRequest;
import com.zzimcong.user.application.dto.UserResponse;
import com.zzimcong.user.common.exception.ConflictException;
import com.zzimcong.user.common.exception.ErrorCode;
import com.zzimcong.user.common.exception.NotFoundException;
import com.zzimcong.user.common.util.AESUtil;
import com.zzimcong.user.domain.entity.User;
import com.zzimcong.user.domain.mapper.UserMapper;
import com.zzimcong.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "USER_SERVICE")
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AESUtil aesUtil;

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(aesUtil.encrypt(email));
    }

    @Transactional
    public UserResponse createUser(SignupRequest signupRequest) {
        String encryptedEmail = aesUtil.encrypt(signupRequest.email());
        if (userRepository.existsByEmail(encryptedEmail)) {
            throw new ConflictException(ErrorCode.DUPLICATE_EMAIL);
        }
        User user = userMapper.toEntity(signupRequest);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserResponse getUserByEmail(String email) {
        return userRepository.findByEmail(aesUtil.encrypt(email))
                .map(userMapper::toDto)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public UserResponse updateUser(Long id, UserModifyRequest userModifyRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateFromDto(userModifyRequest, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    public void signoutUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        user.signOut();
    }
}