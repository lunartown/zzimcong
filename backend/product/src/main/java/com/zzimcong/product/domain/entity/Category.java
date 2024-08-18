package com.zzimcong.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, length = 64)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<Category> childCategories = new ArrayList<>();

    @Column(nullable = false)
    private int depth;

    private String path;

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
        if (parentCategory != null) {
            this.depth = parentCategory.getDepth() + 1;
            this.path = parentCategory.getPath() + "/" + this.id;
        } else {
            this.depth = 0;
            this.path = "/" + this.id;
        }
    }
}