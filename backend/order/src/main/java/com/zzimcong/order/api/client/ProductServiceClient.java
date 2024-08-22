package com.zzimcong.order.api.client;

import com.zzimcong.order.application.dto.ReleaseInventoryRequest;
import com.zzimcong.order.application.dto.ReserveInventoryRequest;
import com.zzimcong.order.application.dto.ReserveInventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductServiceClient {
    @PostMapping("/api/v1/products/{productId}/reserve")
    ReserveInventoryResponse reserveInventory(@PathVariable("productId") Long productId,
                                              @RequestBody ReserveInventoryRequest request);

    @PostMapping("/api/v1/products/{productId}/release")
    void releaseInventory(@PathVariable("productId") Long productId,
                          @RequestBody ReleaseInventoryRequest request);
}