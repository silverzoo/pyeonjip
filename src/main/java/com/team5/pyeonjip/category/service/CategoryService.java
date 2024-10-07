package com.team5.pyeonjip.category.service;

import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.entity.Category;
import com.team5.pyeonjip.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    //NOTE: NPE 방지를 위해 정적메서드로 변환해야해서 mapper를 쓸 수 없음
    //private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getCategories() {
        List<Category> rootCategories = categoryRepository.findByParentIsNull();

        return rootCategories.stream()
                .map(CategoryResponse::toResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        // RuntimeException 은 모두 롤백
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        category = category.toBuilder()
                .name(request.getName())
                .build();

        log.debug("\n\n수정카테고리: {} \n\n", category);

        Category savedCategory = categoryRepository.save(category);

        log.debug("\n\n수정카테고리 res: {} \n\n", savedCategory);

        return CategoryResponse.toResponse(savedCategory);
    }
}
