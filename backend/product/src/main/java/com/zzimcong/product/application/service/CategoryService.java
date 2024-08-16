package com.zzimcong.product.application.service;

import com.zzimcong.product.domain.entity.Category;
import com.zzimcong.product.application.dto.CategoryDto;
import com.zzimcong.product.domain.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //카테고리 목록 조회
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAllWithParent();
        Map<Long, CategoryDto> categoryMap = new HashMap<>();

        for (Category category : categories) {
            categoryMap.put(category.getId(), CategoryDto.of(category));
        }

        List<CategoryDto> rootCategories = new ArrayList<>();
        for (CategoryDto categoryDTO : categoryMap.values()) {
            if (categoryDTO.getParentCategoryId() == null) {
                rootCategories.add(categoryDTO);
            } else {
                CategoryDto parentCategory = categoryMap.get(categoryDTO.getParentCategoryId());
                if (parentCategory != null) {
                    parentCategory.getChildCategories().add(categoryDTO);
                }
            }
        }

        return rootCategories;
    }

    //카테고리 추가
    @Transactional
    public CategoryDto createCategory(String name, Long parentCategoryId) {
        Category parentCategory = null;
        if (parentCategoryId != null) {
            parentCategory = categoryRepository.findById(parentCategoryId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부모 카테고리입니다."));
        }

        Category newCategory = Category.of(name, parentCategory);
        Category savedCategory = categoryRepository.save(newCategory);
        return CategoryDto.of(savedCategory);
    }

    //카테고리 수정
    public CategoryDto updateCategory(Long id, Category category) {
        Category updatedCategory = categoryRepository.findById(id).orElse(null);
        if (updatedCategory != null) {
            updatedCategory.setName(category.getName());
            return CategoryDto.of(categoryRepository.save(updatedCategory));
        } else {
            return null;
        }
    }

    //카테고리 삭제
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
