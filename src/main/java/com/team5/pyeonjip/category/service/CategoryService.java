package com.team5.pyeonjip.category.service;

import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.entity.Category;
import com.team5.pyeonjip.category.mapper.CategoryMapper;
import com.team5.pyeonjip.category.repository.CategoryRepository;
import com.team5.pyeonjip.global.exception.ErrorCode;
import com.team5.pyeonjip.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> newGetCategories() {
        List<Category> allCategories = categoryRepository.findAll();

        List<Category> parentCategories = getParentCategories(allCategories);

        List<CategoryResponse> responses = new ArrayList<>();

        createChildrenCategories(parentCategories, allCategories, responses);

        return responses;
    }

    // 부모-자식 카테고리 연결
    private void createChildrenCategories(List<Category> parentCategories, List<Category> allCategories, List<CategoryResponse> responses) {
        for (Category parent : parentCategories) {
            List<CategoryResponse> children = allCategories.stream()
                    .filter(child -> parent.getId().equals(child.getParentId()))
                    .sorted(Comparator.comparingInt(Category::getSort))
                    .map(categoryMapper::toResponse)
                    .toList();

            CategoryResponse parentResponses = CategoryResponse.builder()
                    .id(parent.getId())
                    .sort(parent.getSort())
                    .name(parent.getName())
                    .child(children)
                    .build();

            responses.add(parentResponses);
        }
    }

    // 최상위 카테고리만 조회
    private static List<Category> getParentCategories(List<Category> allCategories) {
        return allCategories.stream()
                .filter(category -> category.getParentId() == null)
                .sorted(Comparator.comparingInt(Category::getSort))
                .toList();
    }


    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.CATEGORY_NOT_FOUND));

        validateParent(id, request);

        updateSiblingSort(request, category);

        Category updatedCategory = category.toBuilder()
                .name(request.getName())
                .sort(request.getSort())
                .parentId(request.getParentId())
                .build();

        Category savedCategory = categoryRepository.save(updatedCategory);

        return categoryMapper.toResponse(savedCategory);
    }


    // sort 변경으로 인한 형제 카테고리 sort 업데이트
    private void updateSiblingSort(CategoryRequest request, Category category) {
        List<Category> siblings = categoryRepository.findByParentId(category.getParentId());

        for (Category sibling : siblings) {
            if (sibling.getSort() >= request.getSort()) {
                Category updatedSibling = sibling.toBuilder()
                        .sort(sibling.getSort() + 1)
                        .build();
                categoryRepository.save(updatedSibling);
            }
        }
    }

    // 부모 카테고리 유효성 검사
    private void validateParent(Long id, CategoryRequest request) {
        if (request.getParentId().equals(id)) {
            throw new GlobalException(ErrorCode.INVALID_PARENT_SELF);
        } else if (!categoryRepository.existsById(request.getParentId())) {
            throw new GlobalException(ErrorCode.INVALID_PARENT);
        }
    }

    //NOTE: 미사용 코드( newGetCategories() 사용 중 )
    public List<CategoryResponse> getCategories() {
        List<Category> rootCategories = categoryRepository.findByParentIdIsNull();

        return rootCategories.stream()
                .map(categoryMapper::toResponse)
                .toList();
    }
}
