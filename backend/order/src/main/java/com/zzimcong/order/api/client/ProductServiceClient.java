package com.zzimcong.order.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductServiceClient {
//    @GetMapping("/api/v1/products/{productId}")
//    ProductDto getProduct(@PathVariable("productId") Long productId);

    @GetMapping("/api/v1/products/{productId}/stock")
    Integer getProductStock(@PathVariable("productId") Long productId);
}