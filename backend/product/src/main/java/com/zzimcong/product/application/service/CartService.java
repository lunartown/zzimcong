package com.zzimcong.product.application.service;

import com.zzimcong.product.application.dto.CartItemDto;
import com.zzimcong.product.common.exception.BadRequestException;
import com.zzimcong.product.common.exception.ConflictException;
import com.zzimcong.product.common.exception.ErrorCode;
import com.zzimcong.product.common.exception.NotFoundException;
import com.zzimcong.product.domain.entity.CartItem;
import com.zzimcong.product.domain.entity.Product;
import com.zzimcong.product.domain.repository.CartRepository;
import com.zzimcong.product.domain.repository.ProductRepository;
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
    public List<CartItemDto> getCartItemsForUser(Long userId) {
        List<CartItem> cartItems = cartRepository.findByUserId(userId);
        log.info("Retrieved {} cart products for user ID: {}", cartItems.size(), userId);
        return cartItems.stream()
                .map(CartItemDto::of)
                .toList();
    }

    @Transactional
    public void addProductToCart(Long userId, Long productId, int count) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        if (count <= 0) {
            throw new BadRequestException(ErrorCode.INVALID_QUANTITY);
        }

        if (product.getAvailableQuantity() < count) {
            throw new ConflictException(ErrorCode.INSUFFICIENT_STOCK);
        }

        Optional<CartItem> existingCartProduct = cartRepository.findByUserIdAndProductId(userId, productId);

        if (existingCartProduct.isPresent()) {
            CartItem cartItem = existingCartProduct.get();
            cartItem.setCount(cartItem.getCount() + count);
            cartRepository.save(cartItem);
            log.debug("Updated cart product quantity for user ID: {}, product ID: {}", userId, productId);
        } else {
            CartItem newCartItem = new CartItem(userId, product, count);
            cartRepository.save(newCartItem);
            log.debug("Added new product to cart for user ID: {}, product ID: {}", userId, productId);
        }

        productRepository.save(product);
    }

    @Transactional
    public void updateProductCount(Long userId, Long productId, int count) {
        CartItem cartItem = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CART_PRODUCT_NOT_FOUND));

        if (count <= 0) {
            throw new BadRequestException(ErrorCode.INVALID_QUANTITY);
        }

        Product product = cartItem.getProduct();
        int stockDifference = count - cartItem.getCount();

        if (product.getAvailableQuantity() < stockDifference) {
            throw new ConflictException(ErrorCode.INSUFFICIENT_STOCK);
        }

        cartItem.setCount(count);
        cartRepository.save(cartItem);

        log.info("Updated cart product quantity for user ID: {}, product ID: {}, new quantity: {}", userId, productId, count);
    }

    @Transactional
    public void deleteProductFromCart(Long userId, Long productId) {
        CartItem cartItem = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CART_PRODUCT_NOT_FOUND));

        cartRepository.delete(cartItem);
        log.info("Deleted product from cart for user ID: {}, product ID: {}", userId, productId);
    }
}