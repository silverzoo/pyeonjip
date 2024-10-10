package com.team5.pyeonjip.category.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int sort; //낮을수록 먼저 반환

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn (name = "parent_id")
    @Column(name = "parent_id")
    private Long parentId;

//    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL)
    @OneToMany
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private List<Category> children = new ArrayList<>();

    @Builder(toBuilder = true)
    public Category(Long id, String name, int sort, Long parentId, List<Category> children) {
        this.id = id;
        this.name = name;
        this.sort = sort;
        this.parentId = parentId;
        this.children = children;
    }

    // toString() 무한 재귀호출 방지
    @Override
    public String toString() {
        return String.format("Category{id=%d, name='%s'}", id, name);
    }

    public void addChild(Category child) {
        this.children.add(child);
        child.parentId = this.id;
    }

//    public static List<Long> extractLowestCategoryIds(Category category) {
//
//        if (category.getChildren() == null || category.getChildren().isEmpty()) {
//            return List.of(category.getId());
//        }
//
//        return category.getChildren()
//                .stream()
//                //각 Category 객체를 List<Long>으로 변환하여 리스트의 스트림을 생성합니다. 즉, Stream<List<Long>>을 반환
//                .map(Category::extractLowestCategoryIds)
//                // 중첩된 스트림을 평탄화하여 하나의 스트림으로 만듬
//                .flatMap(List::stream)
//                .toList();
//    }
}