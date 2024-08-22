package com.zzimcong.product.api.controller;

import com.zzimcong.product.application.dto.*;
import com.zzimcong.product.application.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "product-controller")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //상품 목록 조회
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId) {
        List<ProductResponseDto> products = productService.getProducts(page, size, search, categoryId);
        return ResponseEntity.ok(products);
    }

    //상품 등록
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto productResponseDto = productService.createProduct(productRequestDto);
        return ResponseEntity.ok(productResponseDto);
    }

    //상품 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto productResponseDto = productService.getProductById(id);
        if (productResponseDto != null) {
            return ResponseEntity.ok(productResponseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //상품 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto updatedProduct = productService.updateProduct(id, productRequestDto);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    //상품 재고 예약
    @PostMapping("/{productId}/reserve")
    public ResponseEntity<ReserveInventoryResponse> reserveInventory(@PathVariable("productId") Long productId,
                                                                     @RequestBody ReserveInventoryRequest request) {
        ReserveInventoryResponse response = productService.reserveInventory(productId, request);
        return ResponseEntity.ok(response);
    }

    //상품 재고 해제
    @PostMapping("/{productId}/release")
    public ResponseEntity<Void> releaseInventory(@PathVariable("productId") Long productId,
                                                 @RequestBody ReleaseInventoryRequest request) {
        productService.releaseInventory(productId, request);
        return ResponseEntity.noContent().build();
    }
}
