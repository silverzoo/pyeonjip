package com.team5.pyeonjip.category.mapper;

import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "parentId", target = "parent.id")
    Category toEntity(CategoryRequest request);

    @Mapping(source = "parent.id", target = "parentId")
    CategoryResponse toResponse(Category category);
}
