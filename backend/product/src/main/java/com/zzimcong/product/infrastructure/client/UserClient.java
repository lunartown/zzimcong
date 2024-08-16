package com.zzimcong.product.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/v1/auth/info")
    String getUserInfo();
}