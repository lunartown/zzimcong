package com.zzimcong.product.domain.repository;

import com.zzimcong.product.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.parentCategory")
    List<Category> findAllWithParent();

    List<Category> findByPathStartingWith(String path);
}
