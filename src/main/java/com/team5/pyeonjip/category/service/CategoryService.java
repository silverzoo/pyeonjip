package com.team5.pyeonjip.category.service;

import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.entity.Category;
import com.team5.pyeonjip.category.mapper.CategoryMapper;
import com.team5.pyeonjip.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;

    public List<CategoryResponse> getCategories() {
        List<Category> rootCategories = categoryRepository.findByParentIsNull();

        return rootCategories.stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        // RuntimeException 은 모두 롤백
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        Category updatedCategory = Category.builder()
                .id(existingCategory.getId())
                .name(request.getName())
                .parent(existingCategory.getParent())
                .child(existingCategory.getChild())
                .build();

        categoryRepository.save(updatedCategory);

        return categoryMapper.toResponse(updatedCategory);
    }
}
