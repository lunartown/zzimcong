package com.lunartown.zzimcong.product.service;

import com.lunartown.zzimcong.product.entity.Category;
import com.lunartown.zzimcong.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //카테고리 목록 조회
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    //카테고리 추가
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    //카테고리 수정
    public Category updateCategory(Long id, Category category) {
        Category updatedCategory = categoryRepository.findById(id).orElse(null);
        if (updatedCategory != null) {
            updatedCategory.setName(category.getName());
            return categoryRepository.save(updatedCategory);
        } else {
            return null;
        }
    }

    //카테고리 삭제
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
