package com.team5.pyeonjip.category.dto;

import com.team5.pyeonjip.category.entity.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CategoryRequest {

    private Long id;

    private String name;

    private Long parentId;

    private List<Category> child = new ArrayList<>();
}
