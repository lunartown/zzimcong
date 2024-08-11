package com.lunartown.zzimcong.product.repository;

import com.lunartown.zzimcong.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
