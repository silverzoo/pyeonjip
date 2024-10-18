package com.team5.pyeonjip.category.service;

import com.team5.pyeonjip.category.dto.CategoryCreateRequest;
import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;

import java.util.*;

public interface CategoryService {

    // 카테고리 전체, 일부 조회
    List<CategoryResponse> getCategories(List<Long> ids);

    // 상위 카테고리 id로 자식 카테고리 id 리스트 조회
    List<Long> getLeafCategoryIds(Long parentId);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    CategoryResponse createCategory(CategoryCreateRequest request);

    Map<String, String> deleteCategories(List<Long> ids);


}
