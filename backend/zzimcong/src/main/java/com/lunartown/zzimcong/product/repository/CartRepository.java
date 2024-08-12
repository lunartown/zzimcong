package com.lunartown.zzimcong.product.repository;

import com.lunartown.zzimcong.product.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartProduct, Long> {
    List<CartProduct> findByUserId(Long userId);

    Optional<CartProduct> findByUserIdAndProductId(Long userId, Long productId);

    void deleteByUserIdAndProductId(Long userId, Long productId);
}
