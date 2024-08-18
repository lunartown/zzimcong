package com.zzimcong.user.api.controller;

import com.zzimcong.user.application.dto.UserModifyRequestDto;
import com.zzimcong.user.application.dto.UserResponseDto;
import com.zzimcong.user.application.service.UserService;
import com.zzimcong.user.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "USER_CONTROLLER")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //회원정보 조회
    @GetMapping
    public ResponseEntity<UserResponseDto> getUserByEmail() {
        UserResponseDto user = userService.getUserById(SecurityUtil.getCurrentUserId());
        return ResponseEntity.ok(user);
    }

    //회원정보 수정
    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserModifyRequestDto userModifyRequestDto) {
        UserResponseDto updatedUser = userService.updateUser(SecurityUtil.getCurrentUserId(), userModifyRequestDto);
        return ResponseEntity.ok(updatedUser);
    }

    //회원탈퇴
    @DeleteMapping("/signout")
    public ResponseEntity<UserResponseDto> signoutUser() {
        UserResponseDto signoutUser = userService.signoutUser(SecurityUtil.getCurrentUserId());
        return ResponseEntity.ok(signoutUser);
    }
}
