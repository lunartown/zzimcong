package com.lunartown.zzimcong.product.controller;

import com.lunartown.zzimcong.product.dto.ProductDto;
import com.lunartown.zzimcong.product.entity.Product;
import com.lunartown.zzimcong.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<ProductDto>> getProducts(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId) {
        try {
            List<ProductDto> productDtos = productService.getProducts(page, size, search, categoryId);
            if (productDtos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
