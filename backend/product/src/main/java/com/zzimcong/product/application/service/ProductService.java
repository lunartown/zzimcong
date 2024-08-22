package com.zzimcong.product.application.service;

import com.zzimcong.product.application.dto.*;
import com.zzimcong.product.domain.entity.Category;
import com.zzimcong.product.domain.entity.Product;
import com.zzimcong.product.domain.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j(topic = "product-service")
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    //상품 등록
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Category category = categoryService.getCategoryById(productRequestDto.getCategoryId());
        Product product = new Product(productRequestDto, category);
        Product createdProduct = productRepository.save(product);
        return new ProductResponseDto(createdProduct);
    }

    //상품 조회
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            return new ProductResponseDto(product);
        } else {
            return null;
        }
    }

    //상품 수정
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        Product updateProduct = productRepository.findById(id).orElse(null);
        if (updateProduct != null) {
            updateProduct.setName(productRequestDto.getName());
            updateProduct.setPrice(productRequestDto.getPrice());
            updateProduct.setSale(productRequestDto.getSale());
            updateProduct.setContent(productRequestDto.getContent());
            updateProduct.setImage(productRequestDto.getImage());
            updateProduct.setAvailableQuantity(productRequestDto.getAvailableQuantity());
            Category category = categoryService.getCategoryById(productRequestDto.getCategoryId());
            updateProduct.setCategory(category);
            Product updatedProduct = productRepository.save(updateProduct);
            return new ProductResponseDto(updatedProduct);
        } else {
            return null;
        }
    }

    public List<ProductResponseDto> getProducts(int page, int size, String search, Long categoryId) {
        List<Long> categoryIds = categoryId != null ?
                categoryService.findAllSubCategoryIds(categoryId) : null;

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAllWithFilters(search, categoryIds, pageable);

        if (productPage.isEmpty()) {
            log.warn("No products found for page: {}, size: {}, search: {}, categoryId: {}", page, size, search, categoryId);
            return Collections.emptyList();
        }

        return productPage.map(ProductResponseDto::of).getContent();
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setDeleted(true);
            productRepository.save(product);
        }
    }

    @Transactional
    public ReserveInventoryResponse reserveInventory(Long productId, ReserveInventoryRequest request) {
        log.info("Attempting to reserve inventory for product ID: {}, Requested quantity: {}", productId, request.getQuantity());
        try {
            // 상품 조회
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
            log.info("Product found: ID={}, Current available quantity={}, Current reserved quantity={}",
                    productId, product.getAvailableQuantity(), product.getReservedQuantity());

            int requestedQuantity = request.getQuantity();

            // 가용 재고 확인
            if (product.getAvailableQuantity() < requestedQuantity) {
                log.warn("Insufficient available inventory. Product ID: {}, Available: {}, Requested: {}",
                        productId, product.getAvailableQuantity(), requestedQuantity);
                throw new IllegalStateException("가용 재고가 부족합니다.");
            }

            // 재고 예약
            int newAvailableQuantity = product.getAvailableQuantity() - requestedQuantity;
            int newReservedQuantity = product.getReservedQuantity() + requestedQuantity;
            product.setAvailableQuantity(newAvailableQuantity);
            product.setReservedQuantity(newReservedQuantity);
            productRepository.save(product);

            log.info("Successfully reserved inventory. Product ID: {}, Reserved: {}, New available quantity: {}, New reserved quantity: {}",
                    productId, requestedQuantity, newAvailableQuantity, newReservedQuantity);

            return new ReserveInventoryResponse(true, "재고 예약 성공");
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Failed to reserve inventory for product ID: {}. Error: {}", productId, e.getMessage(), e);
            return new ReserveInventoryResponse(false, e.getMessage());
        }
    }

    @Transactional
    public void releaseInventory(Long productId, ReleaseInventoryRequest request) {
        log.info("Attempting to release inventory for product ID: {}, Release quantity: {}", productId, request.getQuantity());
        try {
            // 상품 조회
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
            log.info("Product found: ID={}, Current available quantity={}, Current reserved quantity={}",
                    productId, product.getAvailableQuantity(), product.getReservedQuantity());

            int releasedQuantity = request.getQuantity();

            // 예약된 재고 확인
            if (product.getReservedQuantity() < releasedQuantity) {
                log.warn("Insufficient reserved inventory to release. Product ID: {}, Reserved: {}, Requested to release: {}",
                        productId, product.getReservedQuantity(), releasedQuantity);
                throw new IllegalStateException("해제할 수 있는 예약 재고가 부족합니다.");
            }

            // 재고 해제
            int newReservedQuantity = product.getReservedQuantity() - releasedQuantity;
            int newAvailableQuantity = product.getAvailableQuantity() + releasedQuantity;
            product.setReservedQuantity(newReservedQuantity);
            product.setAvailableQuantity(newAvailableQuantity);
            productRepository.save(product);

            log.info("Successfully released inventory. Product ID: {}, Released: {}, New available quantity: {}, New reserved quantity: {}",
                    productId, releasedQuantity, newAvailableQuantity, newReservedQuantity);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Failed to release inventory for product ID: {}. Error: {}", productId, e.getMessage(), e);
            throw e;
        }
    }
}
