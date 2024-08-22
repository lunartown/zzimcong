package com.zzimcong.product.application.service;

import com.zzimcong.product.application.dto.CategoryDto;
import com.zzimcong.product.common.exception.ErrorCode;
import com.zzimcong.product.common.exception.NotFoundException;
import com.zzimcong.product.domain.entity.Category;
import com.zzimcong.product.domain.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAllWithParent();
        log.debug("전체 카테고리 {}개를 조회했습니다.", allCategories.size());

        Map<Long, CategoryDto> categoryDtoMap = new HashMap<>();

        // 모든 카테고리를 DTO로 변환
        for (Category category : allCategories) {
            categoryDtoMap.put(category.getId(), CategoryDto.of(category));
        }

        // 부모-자식 관계 설정 (중복 검사 추가)
        for (Category category : allCategories) {
            if (category.getParentCategory() != null) {
                CategoryDto childDto = categoryDtoMap.get(category.getId());
                CategoryDto parentDto = categoryDtoMap.get(category.getParentCategory().getId());
                if (!parentDto.getChildCategories().contains(childDto)) {
                    parentDto.getChildCategories().add(childDto);
                }
            }
        }

        // 루트 카테고리만 필터링
        List<CategoryDto> rootCategories = categoryDtoMap.values().stream()
                .filter(dto -> dto.getParentCategoryId() == null)
                .collect(Collectors.toList());

        log.debug("최종적으로 {}개의 루트 카테고리를 반환합니다.", rootCategories.size());
        return rootCategories;
    }

    //카테고리 추가
    public Category createCategory(String name, Long parentId) {
        Category category = new Category();
        category.setName(name);

        if (parentId != null) {
            Category parentCategory = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
            category.setParentCategory(parentCategory);
            category.setPath(parentCategory.getPath() + parentCategory.getId() + "/");
        }

        return categoryRepository.save(category);
    }

    //카테고리 수정
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        category.setName(categoryDto.getName());
        categoryRepository.save(category);

        return CategoryDto.of(category);
    }

    //카테고리 삭제
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        categoryRepository.delete(category);
    }


    //서브 카테고리 조회
    public List<Long> findAllSubCategoryIds(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        List<Long> subCategoryIds = new ArrayList<>();
        subCategoryIds.add(categoryId);

        if (category.getPath() != null) {
            List<Category> subCategories = categoryRepository.findByPathStartingWith(category.getPath());
            subCategoryIds.addAll(subCategories.stream().map(Category::getId).toList());
        }

        return subCategoryIds;
    }


    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
