package com.zzimcong.user.domain.mapper;

import com.zzimcong.user.application.dto.SignupRequest;
import com.zzimcong.user.application.dto.UserModifyRequest;
import com.zzimcong.user.application.dto.UserResponse;
import com.zzimcong.user.common.util.AESUtil;
import com.zzimcong.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected AESUtil aesUtil;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "name", expression = "java(aesUtil.encrypt(dto.name()))")
    @Mapping(target = "email", expression = "java(aesUtil.encrypt(dto.email()))")
    @Mapping(target = "phone", expression = "java(aesUtil.encrypt(dto.phone()))")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(dto.password()))")
    public abstract User toEntity(SignupRequest dto);

    @Mapping(target = "email", expression = "java(aesUtil.decrypt(user.getEmail()))")
    @Mapping(target = "username", expression = "java(aesUtil.decrypt(user.getName()))")
    @Mapping(target = "phone", expression = "java(aesUtil.decrypt(user.getPhone()))")
    public abstract UserResponse toDto(User user);

    @Mapping(target = "name", expression = "java(aesUtil.encrypt(dto.name()))")
    @Mapping(target = "phone", expression = "java(aesUtil.encrypt(dto.phone()))")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(dto.password()))")
    public abstract User updateFromDto(UserModifyRequest dto, @MappingTarget User user);
}