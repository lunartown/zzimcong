package com.zzimcong.product.api.controller;

import com.zzimcong.product.application.dto.CategoryCreateRequest;
import com.zzimcong.product.application.dto.CategoryDto;
import com.zzimcong.product.domain.entity.Category;
import com.zzimcong.product.application.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //카테고리 목록 조회
    @GetMapping
    public List<CategoryDto> getCategories() {
        return categoryService.getAllCategories();
    }

    //카테고리 추가
    @PostMapping
    public CategoryDto createCategory(@RequestBody CategoryCreateRequest request) {
        return categoryService.createCategory(request.getName(), request.getParentCategoryId());
    }

    //카테고리 수정
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }

    //카테고리 삭제
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
