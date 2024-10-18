package com.team5.pyeonjip.category.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryResponse {

    private Long id;

    private String name;

    private Integer sort;

    private Long parentId;

    private List<CategoryResponse> children = new ArrayList<>();
}
