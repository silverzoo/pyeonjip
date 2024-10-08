package com.team5.pyeonjip.category.entity;

import com.team5.pyeonjip.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int dept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> child = new ArrayList<>();

    @Builder(toBuilder = true)
    public Category(Long id, String name, Category parent, List<Category> child) {
        this.id = id;
        this.name = name;
        this.dept = parent != null ? parent.getDept() + 1 : 1;
        this.parent = parent;
        this.child = child;
    }

    public void addChildCategory(Category child) {
        this.child.add(child);
        child.parent = this;
    }

    public static List<Long> extractLowestCategoryIds(Category category) {

        if (category.getChild() == null || category.getChild().isEmpty()) {
            return List.of(category.getId());
        }

        return category.getChild()
                .stream()
                //각 Category 객체를 List<Long>으로 변환하여 리스트의 스트림을 생성합니다. 즉, Stream<List<Long>>을 반환
                .map(Category::extractLowestCategoryIds)
                // 중첩된 스트림을 평탄화하여 하나의 스트림으로 만듬
                .flatMap(List::stream)
                .toList();
    }
}