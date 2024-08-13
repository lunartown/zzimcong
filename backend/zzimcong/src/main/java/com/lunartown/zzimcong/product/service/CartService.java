package com.lunartown.zzimcong.product.service;

import com.lunartown.zzimcong.product.dto.CartProductDto;
import com.lunartown.zzimcong.product.entity.CartProduct;
import com.lunartown.zzimcong.product.entity.Product;
import com.lunartown.zzimcong.product.exception.BadRequestException;
import com.lunartown.zzimcong.product.exception.ConflictException;
import com.lunartown.zzimcong.product.exception.ErrorCode;
import com.lunartown.zzimcong.product.exception.NotFoundException;
import com.lunartown.zzimcong.product.repository.CartRepository;
import com.lunartown.zzimcong.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j(topic = "CartService")
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

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
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        if (count <= 0) {
            throw new BadRequestException(ErrorCode.INVALID_QUANTITY);
        }

        if (product.getCount() < count) {
            throw new ConflictException(ErrorCode.INSUFFICIENT_STOCK);
        }

        Optional<CartProduct> existingCartProduct = cartRepository.findByUserIdAndProductId(userId, productId);

        if (existingCartProduct.isPresent()) {
            CartProduct cartProduct = existingCartProduct.get();
            cartProduct.setCount(cartProduct.getCount() + count);
            cartRepository.save(cartProduct);
            log.debug("Updated cart product quantity for user ID: {}, product ID: {}", userId, productId);
        } else {
            CartProduct newCartProduct = new CartProduct(userId, product, count);
            cartRepository.save(newCartProduct);
            log.debug("Added new product to cart for user ID: {}, product ID: {}", userId, productId);
        }

        productRepository.save(product);
    }

    @Transactional
    public void updateProductCount(Long userId, Long productId, int count) {
        CartProduct cartProduct = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CART_PRODUCT_NOT_FOUND));

        if (count <= 0) {
            throw new BadRequestException(ErrorCode.INVALID_QUANTITY);
        }

        Product product = cartProduct.getProduct();
        int stockDifference = count - cartProduct.getCount();

        if (product.getCount() < stockDifference) {
            throw new ConflictException(ErrorCode.INSUFFICIENT_STOCK);
        }

        cartProduct.setCount(count);
        cartRepository.save(cartProduct);

        log.info("Updated cart product quantity for user ID: {}, product ID: {}, new quantity: {}", userId, productId, count);
    }

    @Transactional
    public void deleteProductFromCart(Long userId, Long productId) {
        CartProduct cartProduct = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CART_PRODUCT_NOT_FOUND));

        cartRepository.delete(cartProduct);
        log.info("Deleted product from cart for user ID: {}, product ID: {}", userId, productId);
    }
}