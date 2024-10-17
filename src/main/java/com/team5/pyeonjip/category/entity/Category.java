package com.team5.pyeonjip.category.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ColumnDefault("999")
    private Integer sort; //낮을수록 먼저 반환

    @Column(name = "parent_id")
    private Long parentId;

    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Category> children = new ArrayList<>();

    // toString() 무한 재귀호출 방지
    @Override
    public String toString() {
        return String.format("Category(id=%d, name='%s')", id, name);
    }
}