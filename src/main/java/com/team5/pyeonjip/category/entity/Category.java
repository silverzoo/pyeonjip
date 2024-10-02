package com.team5.pyeonjip.category.entity;

import com.team5.pyeonjip.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int dept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "parent_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<Category> subCategory = new ArrayList<>();

    @Builder
    public Category(String name, int dept,Category parentCategory) {
        this.name = name;
        this.dept = dept;
        this.parentCategory = parentCategory;
    }
}