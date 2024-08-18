package com.zzimcong.user.domain.mapper;

import com.zzimcong.user.application.dto.SignupRequestDto;
import com.zzimcong.user.application.dto.UserModifyRequestDto;
import com.zzimcong.user.application.dto.UserResponseDto;
import com.zzimcong.user.common.exception.InternalServerErrorException;
import com.zzimcong.user.common.util.AESUtil;
import com.zzimcong.user.domain.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private static final Logger logger = LoggerFactory.getLogger(UserMapper.class);
    private final AESUtil aesUtil;
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder, AESUtil aesUtil) {
        this.aesUtil = aesUtil;
        this.passwordEncoder = passwordEncoder;
    }

    //Dto -> Entity 변환
    public User toEntity(SignupRequestDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        try {
            user.setName(aesUtil.encrypt(dto.getName()));
            user.setEmail(aesUtil.encrypt(dto.getEmail()));
            user.setPhone(aesUtil.encrypt(dto.getPhone()));
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        } catch (InternalServerErrorException e) {
            logger.error("Error encrypting user data: {}", e.getMessage());
            throw e;
        }

        return user;
    }

    //Entity -> Dto 변환
    public UserResponseDto toDto(User user) {
        if (user == null) {
            return null;
        }
        UserResponseDto dto = new UserResponseDto();
        try {
            dto.setId(user.getId());
            dto.setEmail(aesUtil.decrypt(user.getEmail()));
            dto.setUsername(aesUtil.decrypt(user.getName()));
            dto.setPhone(aesUtil.decrypt(user.getPhone()));
        } catch (InternalServerErrorException e) {
            logger.error("Error decrypting user data: {}", e.getMessage());
            throw e;
        }

        return dto;
    }

    //Dto를 이용해 Entity 업데이트
    public User updateFromDto(User user, UserModifyRequestDto dto) {
        if (user == null || dto == null) {
            return null;
        }
        user.setName(aesUtil.encrypt(dto.getName()));
        user.setPhone(aesUtil.encrypt(dto.getPhone()));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return user;
    }
}
