package com.zzimcong.order.api.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service")  // URL 제거, 서비스 이름만 사용
public interface UserServiceClient {
//    @GetMapping("/users/{userId}")
//    UserDto getUserById(@PathVariable("userId") Long userId);
}