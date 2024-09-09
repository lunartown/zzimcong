package com.zzimcong.product.application.dto;

import com.zzimcong.product.domain.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDto that = (CategoryDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static CategoryDto of(Category category) {
        return new CategoryDto(category);
    }
}