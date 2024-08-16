package com.zzimcong.product.application.service;

import com.zzimcong.product.domain.entity.Product;
import com.zzimcong.product.application.dto.ProductDto;
import com.zzimcong.product.common.exception.ErrorCode;
import com.zzimcong.product.common.exception.NotFoundException;
import com.zzimcong.product.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //상품 등록
    public ProductDto createProduct(Product product) {
        Product createdProduct = productRepository.save(product);
        if (createdProduct != null) {
            return new ProductDto(createdProduct);
        } else {
            return null;
        }
    }

    //상품 조회
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            return new ProductDto(product);
        } else {
            return null;
        }
    }

    //상품 수정
    public Product updateProduct(Long id, Product product) {
        Product updatedProduct = productRepository.findById(id).orElse(null);
        if (updatedProduct != null) {
            updatedProduct.setName(product.getName());
            updatedProduct.setPrice(product.getPrice());
            updatedProduct.setSale(product.getSale());
            updatedProduct.setContent(product.getContent());
            updatedProduct.setImage(product.getImage());
            updatedProduct.setCount(product.getCount());
            updatedProduct.setCategory(product.getCategory());
            return productRepository.save(updatedProduct);
        } else {
            return null;
        }
    }

    //상품 목록 조회
    public List<ProductDto> getProducts(int page, int size, String search, Long categoryId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAllWithFilters(search, categoryId, pageable);
        //상품이 없을 경우 예외 처리
        if (productPage.isEmpty()) {
            throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return productPage.map(ProductDto::new).getContent();
    }
}