package com.team5.pyeonjip.category.dto;

import com.team5.pyeonjip.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryRequest {

    private Long id;

    private String name;

    private int dept;

    private Long parentId;

    private List<Category> child = new ArrayList<>();
}
