package com.lunartown.zzimcong.product.repository;

import com.lunartown.zzimcong.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE (:search IS NULL OR p.name LIKE %:search%) AND (:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<Product> findAllWithFilters(@Param("search") String search, @Param("categoryId") Long categoryId, Pageable pageable);
}
