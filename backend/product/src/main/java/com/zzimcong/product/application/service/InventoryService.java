package com.zzimcong.product.application.service;

import com.zzimcong.product.domain.entity.InventoryEvent;
import com.zzimcong.product.domain.entity.InventoryEventType;
import com.zzimcong.product.domain.entity.Product;
import com.zzimcong.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, InventoryEvent> kafkaTemplate;

    @Transactional
    public boolean reserveStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다."));

        if (product.getAvailableQuantity() >= quantity) {
            product.setReservedQuantity(product.getReservedQuantity() + quantity);
            product.setAvailableQuantity(product.getAvailableQuantity() - quantity);
            productRepository.save(product);

            kafkaTemplate.send("inventory-events", new InventoryEvent(productId, InventoryEventType.RESERVED, quantity));
            return true;
        }
        return false;
    }

    @Transactional
    public void confirmStockReduction(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다."));

        product.setReservedQuantity(product.getReservedQuantity() - quantity);
        productRepository.save(product);

        kafkaTemplate.send("inventory-events", new InventoryEvent(productId, InventoryEventType.CONFIRMED, quantity));
    }

    @Transactional
    public void cancelStockReservation(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다."));

        product.setReservedQuantity(product.getReservedQuantity() - quantity);
        product.setAvailableQuantity(product.getAvailableQuantity() + quantity);
        productRepository.save(product);

        kafkaTemplate.send("inventory-events", new InventoryEvent(productId, InventoryEventType.CANCELLED, quantity));
    }

    @Transactional
    public void restoreStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다."));

        product.setAvailableQuantity(product.getAvailableQuantity() + quantity);
        productRepository.save(product);

        kafkaTemplate.send("inventory-events", new InventoryEvent(productId, InventoryEventType.RESTORED, quantity));
    }

    public int getAvailableStock(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다."));
        return product.getAvailableQuantity();
    }
}