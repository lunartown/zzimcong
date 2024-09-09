package com.zzimcong.product.application.service;

import com.zzimcong.product.application.dto.ProductRequestDto;
import com.zzimcong.product.application.dto.ProductResponseDto;
import com.zzimcong.product.domain.entity.Category;
import com.zzimcong.product.domain.entity.Product;
import com.zzimcong.product.domain.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
            updateProduct.setStock(productRequestDto.getStock());
            Category category = categoryService.getCategoryById(productRequestDto.getCategoryId());
            updateProduct.setCategory(category);
            Product updatedProduct = productRepository.save(updateProduct);
            return new ProductResponseDto(updatedProduct);
        } else {
            return null;
        }
    }

    //상품 목록 조회
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

    //상품 삭제
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setDeleted(true);
            productRepository.save(product);
        }
    }
}
