package com.team5.pyeonjip.category.mapper;

import com.team5.pyeonjip.category.dto.CategoryCreateRequest;
import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryRequest request);

    Category toEntity(CategoryCreateRequest request);

    CategoryResponse toResponse(Category category);
}
