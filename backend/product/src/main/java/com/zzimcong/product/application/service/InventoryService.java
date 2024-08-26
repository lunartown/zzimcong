package com.zzimcong.product.application.service;

import com.zzimcong.product.domain.entity.Product;
import com.zzimcong.product.domain.repository.ProductRepository;
import com.zzimcong.zzimconginventorycore.common.event.InventoryUpdateEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j(topic = "INVENTORY-SERVICE")
@RequiredArgsConstructor
public class InventoryService {
    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void initializeRedisCache() {
        log.info("Initializing Redis cache with product inventory");
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            updateRedisInventory(product.getId(), product.getStock());
        }
    }

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void syncInventoryFromRedis() {
        log.info("Syncing inventory from Redis to database");
        Set<String> keys = redisTemplate.keys("inventory:*");
        for (String key : keys) {
            try {
                String productId = key.split(":")[1];
                Integer redisStock = (Integer) redisTemplate.opsForValue().get(key);
                if (redisStock != null) {
                    updateDatabaseInventory(Long.parseLong(productId), redisStock);
                }
            } catch (Exception e) {
                log.error("Error syncing inventory for key: {}", key, e);
            }
        }
    }

    @KafkaListener(topics = "inventory-update")
    public void handleInventoryUpdateEvent(InventoryUpdateEvent event) {
        log.info("Processing inventory update event for product ID: {}", event.getProductId());
        updateDatabaseInventory(event.getProductId(), -event.getQuantity());
    }

    @Transactional
    public void updateStock(Long productId, int newStock) {
        log.info("Updating stock for product ID: {} to {}", productId, newStock);
        updateDatabaseInventory(productId, newStock);
        updateRedisInventory(productId, newStock);
    }

    private void updateRedisInventory(Long productId, int stock) {
        try {
            redisTemplate.opsForValue().set("inventory:" + productId, stock);
        } catch (Exception e) {
            log.error("Error updating Redis inventory for product ID: {}", productId, e);
        }
    }

    private void updateDatabaseInventory(Long productId, int stockChange) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
            int newStock = product.getStock() + stockChange;
            product.setStock(newStock);
            productRepository.save(product);
        } catch (Exception e) {
            log.error("Error updating database inventory for product ID: {}", productId, e);
        }
    }
}