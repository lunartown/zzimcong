package com.zzimcong.order.application.service;

import com.zzimcong.order.api.client.ProductServiceClient;
import com.zzimcong.order.application.dto.ReleaseInventoryRequest;
import com.zzimcong.order.application.dto.ReserveInventoryRequest;
import com.zzimcong.order.application.dto.ReserveInventoryResponse;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductServiceClient productServiceClient;

    @Autowired
    public ProductService(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    public boolean reserveInventory(Long productId, int quantity) {
        try {
            ReserveInventoryResponse response = productServiceClient.reserveInventory(
                    productId, new ReserveInventoryRequest(quantity));
            return response.success();
        } catch (FeignException e) {
            if (e.status() == HttpStatus.BAD_REQUEST.value()) {
                // 재고 부족 등의 이유로 예약 실패
                return false;
            }
            throw new RuntimeException("재고 예약 중 오류 발생", e);
        }
    }

    public void releaseInventory(Long productId, int quantity) {
        productServiceClient.releaseInventory(productId, new ReleaseInventoryRequest(quantity));
    }
}