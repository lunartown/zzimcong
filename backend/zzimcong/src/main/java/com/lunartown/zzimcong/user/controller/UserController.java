package com.lunartown.zzimcong.user.controller;

import com.lunartown.zzimcong.user.dto.EmailRequestDto;
import com.lunartown.zzimcong.user.dto.UserModifyRequestDto;
import com.lunartown.zzimcong.user.dto.UserResponseDto;
import com.lunartown.zzimcong.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
