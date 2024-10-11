package com.team5.pyeonjip.category.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class CategoryResponse {

    private Long id;

    private String name;

    private int sort;

    private Long parentId;

    private List<CategoryResponse> children = new ArrayList<>();
}
