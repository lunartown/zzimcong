package com.lunartown.zzimcong.product.dto;

import com.lunartown.zzimcong.product.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private Long parentCategoryId;
    private List<CategoryDto> childCategories;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.parentCategoryId = category.getParentCategory() != null ? category.getParentCategory().getId() : null;
        this.childCategories = category.getChildCategories().stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    public static CategoryDto of(Category category) {
        return new CategoryDto(category);
    }
}