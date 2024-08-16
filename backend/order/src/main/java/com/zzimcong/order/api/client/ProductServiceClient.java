package com.zzimcong.order.api.client;

import com.zzimcong.order.application.dto.CartItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductServiceClient {
    //product
    @PostMapping("/api/v1/products/check-inventory")
    boolean checkInventory(@RequestBody InventoryCheckRequest request);

    //cart
    @GetMapping("/api/v1/cart")
    List<CartItemDto> getMyCart(@AuthenticationPrincipal UserDetails userDetails);
}