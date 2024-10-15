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
    private Integer sort; //낮을수록 먼저 반환

    @Column(name = "parent_id")
    private Long parentId;

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
        return String.format("Category(id=%d, name='%s')", id, name);
    }
}