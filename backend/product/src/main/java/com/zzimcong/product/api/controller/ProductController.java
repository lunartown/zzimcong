package com.zzimcong.product.api.controller;

import com.zzimcong.product.application.dto.ProductDto;
import com.zzimcong.product.application.service.ProductService;
import com.zzimcong.product.domain.entity.Product;
import com.zzimcong.product.infrastructure.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final UserClient userClient;

    @Autowired
    public ProductController(ProductService productService, UserClient userClient) {
        this.productService = productService;
        this.userClient = userClient;
    }

    //상품 목록 조회
    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId) {
        List<ProductDto> products = productService.getProducts(page, size, search, categoryId);
        return ResponseEntity.ok(products);
    }

    //상품 등록
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody Product product) {
        ProductDto productDto = productService.createProduct(product);
        return ResponseEntity.ok(productDto);
    }

    //상품 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
        if (productDto != null) {
            return ResponseEntity.ok(productDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //상품 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
