package com.team5.pyeonjip.category.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

//@Setter
//@DynamicUpdate
// Setter 와 DynamicUpdate 활용해서 수정 값만 변경 가능. Dirty Checking 효율 UP

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
    @Comment("오름차순으로 반환")
    private Integer sort;

    @Column(name = "parent_id")
    private Long parentId;

    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Category> children = new ArrayList<>();

    @Builder(toBuilder = true)
    public Category(Long id, String name, Integer sort, Long parentId, List<Category> children) {
        this.id = id;
        this.name = name;
        this.sort = sort;
        this.parentId = parentId;
        this.children = children;
    }

    // toString() 무한 재귀호출 방지
    @Override
    public String toString() {
        return String.format("Category(id=%d, name='%s', sort='%d')", id, name, sort);
    }
}