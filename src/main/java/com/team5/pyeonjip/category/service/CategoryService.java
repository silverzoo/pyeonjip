package com.team5.pyeonjip.category.service;

import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.entity.Category;
import com.team5.pyeonjip.category.mapper.CategoryMapper;
import com.team5.pyeonjip.category.repository.CategoryRepository;
import com.team5.pyeonjip.category.utils.CategoryUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryUtils categoryUtils;

    public List<CategoryResponse> newGetCategories() {
        List<Category> allCategories = categoryRepository.findAll();

        List<Category> parentCategories = categoryUtils.getParentCategories(allCategories);

        List<CategoryResponse> responses = new ArrayList<>();

        categoryUtils.createChildrenCategories(parentCategories, allCategories, responses);

        return responses;
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        Category category = categoryUtils.getCategory(id);

        categoryUtils.validateParent(id, request);

        categoryUtils.updateSiblingSort(request, category);

        Category updatedCategory = category.toBuilder()
                .name(request.getName())
                .sort(request.getSort())
                .parentId(request.getParentId())
                .build();

        Category savedCategory = categoryRepository.save(updatedCategory);

        return categoryMapper.toResponse(savedCategory);
    }

    //NOTE: 미사용 코드( newGetCategories() 사용 중 )
    public List<CategoryResponse> getCategories() {
        List<Category> rootCategories = categoryRepository.findByParentIdIsNull();

        return rootCategories.stream()
                .map(categoryMapper::toResponse)
                .toList();
    }
}
