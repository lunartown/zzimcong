package com.zzimcong.product.application.service;

import com.zzimcong.product.application.dto.CategoryDto;
import com.zzimcong.product.common.exception.ErrorCode;
import com.zzimcong.product.common.exception.NotFoundException;
import com.zzimcong.product.domain.entity.Category;
import com.zzimcong.product.domain.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        }

        category = categoryRepository.save(category);

        // ID가 생성된 후 path 업데이트
        if (parentId == null) {
            category.setPath("/" + category.getId());
        } else {
            Category parentCategory = category.getParentCategory();
            category.setPath(parentCategory.getPath() + "/" + category.getId());
        }

        return categoryRepository.save(category);
    }

    //카테고리 수정
    public void updateCategoryHierarchy() {
        List<Category> allCategories = categoryRepository.findAll();
        for (Category category : allCategories) {
            updateCategoryDepthAndPath(category);
        }
        categoryRepository.saveAll(allCategories);
    }

    //카테고리 수정
    private void updateCategoryDepthAndPath(Category category) {
        if (category.getParentCategory() == null) {
            category.setDepth(0);
            category.setPath("/" + category.getId());
        } else {
            category.setDepth(category.getParentCategory().getDepth() + 1);
            category.setPath(category.getParentCategory().getPath() + "/" + category.getId());
        }
    }

    //카테고리 삭제
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    //카테고리 ID로 하위 카테고리 ID 목록 조회
    public List<Long> findAllSubCategoryIds(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        return categoryRepository.findByPathStartingWith(category.getPath())
                .stream()
                .map(Category::getId)
                .collect(Collectors.toList());
    }


}
