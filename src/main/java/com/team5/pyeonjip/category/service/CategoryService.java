package com.team5.pyeonjip.category.service;

import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.entity.Category;
import com.team5.pyeonjip.category.mapper.CategoryMapper;
import com.team5.pyeonjip.category.repository.CategoryRepository;
import com.team5.pyeonjip.global.exception.InvalidParentException;
import com.team5.pyeonjip.global.exception.ResourceNotFoundException;
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

        List<Category> parentCategories = allCategories.stream()
                .filter(category -> category.getParentId() == null)
                .sorted(Comparator.comparingInt(Category::getSort))
                .toList();

        List<CategoryResponse> responses = new ArrayList<>();

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

        return responses;
    }

    //NOTE: 미사용 코드( newGetCategories() 사용 중 )
    public List<CategoryResponse> getCategories() {
        List<Category> rootCategories = categoryRepository.findByParentIdIsNull();

        return rootCategories.stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 카테고리를 찾을 수 없습니다: " + id));

        if (request.getParentId() != null && (request.getParentId().equals(id) || !categoryRepository.existsById(request.getParentId()))) {
            throw new InvalidParentException("해당 카테고리를 상위 카테고리로 수정할 수 없습니다.");
        }

        //sort를 수정할 때 형제 카테고리의 sort도 업데이트해준다.
        List<Category> siblings = categoryRepository.findByParentId(category.getParentId());

        for (Category sibling : siblings) {
            if (sibling.getSort() >= request.getSort()) {
                Category updatedSibling = sibling.toBuilder()
                        .sort(sibling.getSort() + 1)
                        .build();
                categoryRepository.save(updatedSibling);
            }
        }

        Category updatedCategory = category.toBuilder()
                .name(request.getName())
                .sort(request.getSort())
                .parentId(request.getParentId())
                .build();

        Category savedCategory = categoryRepository.save(updatedCategory);

        return categoryMapper.toResponse(savedCategory);
    }
}
