package com.zzimcong.user.application.service;

import com.zzimcong.user.common.exception.BadRequestException;
import com.zzimcong.user.common.util.AESUtil;
import com.zzimcong.user.application.dto.SignupRequestDto;
import com.zzimcong.user.domain.entity.User;
import com.zzimcong.user.application.dto.UserModifyRequestDto;
import com.zzimcong.user.application.dto.UserResponseDto;
import com.zzimcong.user.common.exception.ConflictException;
import com.zzimcong.user.common.exception.ErrorCode;
import com.zzimcong.user.common.exception.NotFoundException;
import com.zzimcong.user.domain.mapper.UserMapper;
import com.zzimcong.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j(topic = "USER_SERVICE")
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AESUtil aesUtil;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, AESUtil aesUtil) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.aesUtil = aesUtil;
    }

    //이메일 중복 체크
    public boolean checkEmailAvailability(String email) {
        return !userRepository.existsByEmail(aesUtil.encrypt(email));
    }

    //유저 생성
    public User createUser(SignupRequestDto signupRequestDto) {
        if (userRepository.existsByEmail(aesUtil.encrypt(signupRequestDto.getEmail()))) {
            throw new ConflictException(ErrorCode.DUPLICATE_EMAIL);
        }
        User user = userMapper.toEntity(signupRequestDto);
        return userRepository.save(user);
    }

    //유저 조회
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(aesUtil.encrypt(email))
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toDto(user);
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toDto(user);
    }

    //유저 수정
    @Transactional
    public UserResponseDto updateUser(UserModifyRequestDto userModifyRequestDto) {
        User findUser = userRepository.findByEmail(aesUtil.encrypt(userModifyRequestDto.getEmail()))
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        findUser = userMapper.updateFromDto(findUser, userModifyRequestDto);
        User updatedUser = userRepository.save(findUser);
        return userMapper.toDto(updatedUser);
    }

    //유저 삭제
    public UserResponseDto signoutUser(String email) {
        User findUser = userRepository.findByEmail(aesUtil.encrypt(email))
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        findUser.setSignout(true);
        User signoutUser = userRepository.save(findUser);
        return userMapper.toDto(signoutUser);
    }
}