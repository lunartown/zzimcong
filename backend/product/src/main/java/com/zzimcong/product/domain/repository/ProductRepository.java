package com.zzimcong.product.domain.repository;

import com.zzimcong.product.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE (:search IS NULL OR p.name LIKE %:search%) AND p.category.id IN :categoryIds")
    Page<Product> findAllWithFilters(
            @Param("search") String search,
            @Param("categoryIds") List<Long> categoryIds,
            Pageable pageable
    );
}
