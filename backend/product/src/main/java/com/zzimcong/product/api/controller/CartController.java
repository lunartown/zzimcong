package com.zzimcong.product.api.controller;

import com.zzimcong.product.application.dto.CartItemDto;
import com.zzimcong.product.application.service.CartService;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getMyCart(@RequestHeader("X-Auth-User-ID") Long userId) {
        List<CartItemDto> cart = cartService.getCartItemsForUser(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<String> addProductToCart(
            @RequestHeader("X-Auth-User-ID") Long userId,
            @PathVariable Long productId,
            @RequestParam @Min(value = 1, message = "수량은 1 이상이어야 합니다.") int count) {
        cartService.addProductToCart(userId, productId, count);
        return ResponseEntity.status(HttpStatus.CREATED).body("장바구니 추가 완료");
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<String> updateProductCount(
            @RequestHeader("X-Auth-User-ID") Long userId,
            @PathVariable Long productId,
            @RequestParam @Min(value = 1, message = "수량은 1 이상이어야 합니다.") int count) {
        cartService.updateProductCount(userId, productId, count);
        return ResponseEntity.ok("수량 업데이트 완료");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductFromCart(
            @RequestHeader("X-Auth-User-ID") Long userId,
            @PathVariable Long productId) {
        cartService.deleteProductFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
