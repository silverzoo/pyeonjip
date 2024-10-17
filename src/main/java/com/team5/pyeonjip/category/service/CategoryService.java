package com.team5.pyeonjip.category.service;

import com.team5.pyeonjip.category.dto.CategoryCreateRequest;
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

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryUtils categoryUtils;

    // 카테고리 전체, 일부 조회
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories(List<Long> ids) {

        // id 리스트가 없으면 전체 조회, 있으면 부분 조회
        if (ids == null || ids.isEmpty()) {

            List<Category> allCategories = categoryRepository.findAll();
            List<Category> parentCategories = categoryUtils.getParentCategories(allCategories);

            return categoryUtils.createChildrenCategories(parentCategories, allCategories);

        } else {

            List<Category> categories = categoryUtils.validateAndFindCategory(ids);

            return categories.stream()
                    .map(categoryMapper::toResponse)
                    .toList();
        }
    }

    // 상위 카테고리 id로 자식 카테고리 id 리스트 조회
    @Transactional(readOnly = true)
    public List<Long> getLeafCategoryIds(Long parentId) {

        categoryUtils.validateAndFindCategory(parentId);

        return categoryRepository.findLeafCategories(parentId);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        Category old = categoryUtils.validateAndFindCategory(id);

        categoryUtils.validateNoChanges(request, old);

        if (!Objects.equals(request.getParentId(), old.getParentId())) {
            categoryUtils.validateParent(request);
        }

        if (!request.getName().equals(old.getName())) {
            categoryUtils.validateName(request.getName());
        }

        if (!request.getSort().equals(old.getSort())) {
            categoryUtils.updateSiblingSort(old, request);
        }

        Category updatedCategory = old.toBuilder()
                .id(id)
                .name(request.getName() != null ? request.getName() : old.getName())
                .sort(request.getSort() != null ? request.getSort() : old.getSort())
                .parentId(request.getParentId() != null ? request.getParentId() : null)
                .build();

        return categoryMapper.toResponse(categoryRepository.save(updatedCategory));
    }



    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {

        categoryUtils.validateName(request.getName());

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
