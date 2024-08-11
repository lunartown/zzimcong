package com.lunartown.zzimcong.user.service;

import com.lunartown.zzimcong.common.util.AESUtil;
import com.lunartown.zzimcong.user.dto.SignupRequestDto;
import com.lunartown.zzimcong.user.dto.UserModifyRequestDto;
import com.lunartown.zzimcong.user.dto.UserResponseDto;
import com.lunartown.zzimcong.user.entity.User;
import com.lunartown.zzimcong.user.exception.DuplicateEmailException;
import com.lunartown.zzimcong.user.exception.UserNotFoundException;
import com.lunartown.zzimcong.user.mapper.UserMapper;
import com.lunartown.zzimcong.user.repository.UserRepository;
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
            throw new DuplicateEmailException("이미 존재하는 이메일입니다: " + signupRequestDto.getEmail());
        }
        User user = userMapper.toEntity(signupRequestDto);
        return userRepository.save(user);
    }

    //유저 조회
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(aesUtil.encrypt(email))
                .orElseThrow(() -> new UserNotFoundException("해당 유저가 없습니다."));
        return userMapper.toDto(user);
    }

    //유저 수정
    @Transactional
    public UserResponseDto updateUser(UserModifyRequestDto userModifyRequestDto) {
        User findUser = userRepository.findByEmail(aesUtil.encrypt(userModifyRequestDto.getEmail()))
                .orElseThrow(() -> new UserNotFoundException("해당 유저가 없습니다."));
        findUser = userMapper.updateFromDto(findUser, userModifyRequestDto);
        User updatedUser = userRepository.save(findUser);
        return userMapper.toDto(updatedUser);
    }

    //유저 삭제
    public UserResponseDto signoutUser(String email) {
        User findUser = userRepository.findByEmail(aesUtil.encrypt(email))
                .orElseThrow(() -> new UserNotFoundException("해당 유저가 없습니다."));
        findUser.setSignout(true);
        User signoutUser = userRepository.save(findUser);
        return userMapper.toDto(signoutUser);
    }
}
