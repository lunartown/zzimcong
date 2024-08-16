package com.zzimcong.auth.api.controller;

import com.zzimcong.auth.application.dto.EmailRequestDto;
import com.zzimcong.auth.application.dto.UserModifyRequestDto;
import com.zzimcong.auth.application.dto.UserResponseDto;
import com.zzimcong.auth.application.service.UserService;
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

    //회원정보
    @GetMapping
    public ResponseEntity<UserResponseDto> getUserByEmail(@RequestBody EmailRequestDto emailRequestDto) {
        UserResponseDto user = userService.getUserByEmail(emailRequestDto.getEmail());
        return ResponseEntity.ok(user);
    }

    //회원정보 수정
    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserModifyRequestDto userModifyRequestDto) {
        UserResponseDto updatedUser = userService.updateUser(userModifyRequestDto);
        return ResponseEntity.ok(updatedUser);
    }

    //회원탈퇴
    @PostMapping("/signout")
    public ResponseEntity<UserResponseDto> signoutUser(@RequestBody EmailRequestDto emailRequestDto) {
        UserResponseDto signoutUser = userService.signoutUser(emailRequestDto.getEmail());
        return ResponseEntity.ok(signoutUser);
    }
}
