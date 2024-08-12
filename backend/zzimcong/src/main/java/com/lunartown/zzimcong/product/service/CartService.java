package com.lunartown.zzimcong.product.service;

import com.lunartown.zzimcong.product.dto.CartProductDto;
import com.lunartown.zzimcong.product.entity.CartProduct;
import com.lunartown.zzimcong.product.entity.Product;
import com.lunartown.zzimcong.product.repository.CartRepository;
import com.lunartown.zzimcong.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j(topic = "CART_SERVICE")
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<CartProductDto> getCartProductsForUser(Long userId) {
        List<CartProduct> cartProducts = cartRepository.findByUserId(userId);
        log.info("Retrieved {} cart products for user ID: {}", cartProducts.size(), userId);
        return cartProducts.stream()
                .map(CartProductDto::of)
                .toList();
    }

    @Transactional
    public void addProductToCart(Long userId, Long productId, int count) {
        // 상품 존재 여부 확인
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다. ID: " + productId));

        // 수량 유효성 검사
        if (count <= 0) {
            throw new IllegalArgumentException("상품 수량은 1 이상이어야 합니다.");
        }

        // 이미 장바구니에 있는 상품인지 확인
        Optional<CartProduct> existingCartProduct = cartRepository.findByUserIdAndProductId(userId, productId);

        if (existingCartProduct.isPresent()) {
            // 이미 있다면 수량 업데이트
            CartProduct cartProduct = existingCartProduct.get();
            cartProduct.setCount(cartProduct.getCount() + count);
            cartRepository.save(cartProduct);
            log.info("Updated cart product quantity for user ID: {}, product ID: {}", userId, productId);
        } else {
            // 없다면 새로 추가
            CartProduct newCartProduct = new CartProduct(userId, product, count);
            cartRepository.save(newCartProduct);
            log.info("Added new product to cart for user ID: {}, product ID: {}", userId, productId);
        }
    }

    @Transactional
    public void updateProductCount(Long userId, Long productId, int count) {
        // 장바구니 상품 존재 여부 확인
        CartProduct cartProduct = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new EntityNotFoundException("장바구니에서 상품을 찾을 수 없습니다. 사용자 ID: " + userId + ", 상품 ID: " + productId));

        // 수량 유효성 검사
        if (count <= 0) {
            throw new IllegalArgumentException("상품 수량은 1 이상이어야 합니다.");
        }

        cartProduct.setCount(count);
        cartRepository.save(cartProduct);
        log.info("Updated cart product quantity for user ID: {}, product ID: {}, new quantity: {}", userId, productId, count);
    }

    @Transactional
    public void deleteProductFromCart(Long userId, Long productId) {
        // 장바구니 상품 존재 여부 확인
        CartProduct cartProduct = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new EntityNotFoundException("장바구니에서 상품을 찾을 수 없습니다. 사용자 ID: " + userId + ", 상품 ID: " + productId));

        cartRepository.delete(cartProduct);
        log.info("Deleted product from cart for user ID: {}, product ID: {}", userId, productId);
    }
}