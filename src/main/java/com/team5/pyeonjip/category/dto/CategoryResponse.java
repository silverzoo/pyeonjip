package com.team5.pyeonjip.category.dto;

import com.team5.pyeonjip.category.entity.Category;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class CategoryResponse {

    private Long id;

    private String name;

    private int dept;

    private Long parentId;

    private List<CategoryResponse> child = new ArrayList<>();

    public static CategoryResponse toResponse(Category category) {

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDept(),
                category.getParent() != null ? category.getParent().getId() : null,
                category.getChild()
                        .stream()
                        .map(CategoryResponse::toResponse)
                        .toList()
        );
    }
}
