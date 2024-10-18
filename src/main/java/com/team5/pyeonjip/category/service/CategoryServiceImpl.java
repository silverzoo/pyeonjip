package com.team5.pyeonjip.category.service;

import com.team5.pyeonjip.category.dto.CategoryCreateRequest;
import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.entity.Category;
import com.team5.pyeonjip.category.mapper.CategoryMapper;
import com.team5.pyeonjip.category.repository.CategoryRepository;
import com.team5.pyeonjip.category.utils.CategoryUtils;
import com.team5.pyeonjip.category.utils.CategoryValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryUtils categoryUtils;
    private final CategoryValidate categoryValidate;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories(List<Long> ids) {

        // id 리스트가 없으면 전체 조회, 있으면 부분 조회
        if (ids == null || ids.isEmpty()) {

            List<Category> allCategories = categoryRepository.findAll();
            List<Category> parentCategories = categoryUtils.getParentCategories(allCategories);

            return categoryUtils.createChildrenCategories(parentCategories, allCategories);

        } else {

            List<Category> categories = categoryValidate.validateAndFindCategory(ids);

            return categories.stream()
                    .map(categoryMapper::toResponse)
                    .toList();
        }
    }

    @Transactional(readOnly = true)
    public List<Long> getLeafCategoryIds(Long parentId) {

        categoryValidate.validateAndFindCategory(parentId);

        return categoryRepository.findLeafCategories(parentId);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        Category old = categoryValidate.validateAndFindCategory(id);

        if (!Objects.equals(request.getParentId(), old.getParentId())) {
            categoryValidate.validateParent(request);
            old = old.toBuilder().parentId(request.getParentId() != null ? request.getParentId() : null).build();
        }

        if (!Objects.equals(request.getName(), old.getName())) {
            categoryValidate.validateName(request.getName());
            old = old.toBuilder().name(request.getName() != null ? request.getName() : old.getName()).build();
        }

        if (!Objects.equals(request.getSort(), old.getSort())) {
            categoryUtils.updateSiblingSort(old, request);
            old = old.toBuilder().sort(request.getSort() != null ? request.getSort() : old.getSort()).build();
        }

        return categoryMapper.toResponse(categoryRepository.save(old));
    }

    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {

        categoryValidate.validateName(request.getName());

        Category category = categoryMapper.toEntity(request);

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public Map<String, String> deleteCategories(List<Long> ids) {

        Map<String, String> response = new HashMap<>();

        // id 리스트가 없으면 삭제 x, 있으면 부분 삭제
        if (ids == null || ids.isEmpty()) {

            response.put("message", "삭제할 카테고리가 없습니다.");

        } else {

            categoryUtils.deleteCategoriesAndUpdateProducts(ids);
            response.put("message", "카테고리가 삭제되었습니다.");
        }

        return response;
    }
}
