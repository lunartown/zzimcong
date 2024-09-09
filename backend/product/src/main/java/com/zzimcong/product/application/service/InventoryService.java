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
import java.util.concurrent.TimeUnit;

@Service
@Slf4j(topic = "INVENTORY-SERVICE")
@RequiredArgsConstructor
public class InventoryService {
    private final ProductRepository productRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void initializeRedisCache() {
        log.info("Redis 캐시 초기화 시작");
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            updateRedisInventory(product.getId(), product.getStock());
        }
        log.info("Redis 캐시 초기화 완료: {} 개 상품 처리됨", products.size());
    }

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void syncInventoryFromDB() {
        log.info("데이터베이스에서 Redis로 재고 동기화 시작");
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            updateRedisInventory(product.getId(), product.getStock());
        }
        log.info("재고 동기화 완료: {} 개 상품 처리됨", products.size());
    }

    @KafkaListener(topics = "inventory-update")
    public void handleInventoryUpdateEvent(InventoryUpdateEvent event) {
        log.info("재고 업데이트 이벤트 수신: 상품 ID = {}, 수량 변경 = {}", event.getProductId(), event.getQuantity());
        updateDBInventory(event.getProductId(), -event.getQuantity());
    }

    @Transactional
    public void updateDBStock(Long productId, int stockChange) {
        log.info("DB 재고 변경 요청: 상품 ID = {}, 변경량 = {}", productId, stockChange);
        updateDBInventory(productId, stockChange);
    }

    public int getRedisStock(Long productId) {
        String stock = redisTemplate.opsForValue().get("inventory:" + productId);
        return stock != null ? Integer.parseInt(stock) : -1;
    }

    private void updateDBInventory(Long productId, int stockChange) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없음: " + productId));

            int currentStock = product.getStock();
            int newStock = currentStock + stockChange;

            if (newStock < 0) {
                throw new IllegalStateException("재고는 음수가 될 수 없습니다. 상품 ID: " + productId + ", 현재 재고: " + currentStock + ", 변경량: " + stockChange);
            }

            product.setStock(newStock);
            productRepository.save(product);

            log.info("DB 재고 업데이트 완료: 상품 ID = {}, 이전 재고 = {}, 변경량 = {}, 새 재고 = {}",
                    productId, currentStock, stockChange, newStock);
        } catch (Exception e) {
            log.error("DB 재고 업데이트 중 오류 발생: 상품 ID = {}, 변경량 = {}", productId, stockChange, e);
            throw new RuntimeException("DB 재고 업데이트 실패", e);
        }
    }

    private void updateRedisInventory(Long productId, int stock) {
        try {
            String inventoryKey = "inventory:" + productId;
            redisTemplate.opsForValue().set(inventoryKey, String.valueOf(stock));
            redisTemplate.expire(inventoryKey, 1, TimeUnit.DAYS);

            log.info("Redis 재고 업데이트 완료: 상품 ID = {}, 재고 = {}", productId, stock);
        } catch (Exception e) {
            log.error("Redis 재고 업데이트 중 오류 발생: 상품 ID = {}", productId, e);
        }
    }
}