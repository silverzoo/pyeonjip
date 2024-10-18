package com.team5.pyeonjip.category.utils;

import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.entity.Category;
import com.team5.pyeonjip.category.repository.CategoryRepository;
import com.team5.pyeonjip.global.exception.ErrorCode;
import com.team5.pyeonjip.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryValidate {
    private final CategoryRepository categoryRepository;

    // id 유효성 검사
    public Category validateAndFindCategory(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public List<Category> validateAndFindCategory(List<Long> ids) {

        List<Category> categories = categoryRepository.findAllById(ids);

        if (categories.size() != ids.size()) {
            throw new GlobalException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        return categories;

    }

    // 부모카테고리 유효성 검사
    public void validateParent(CategoryRequest request) {

        Long requestParentId = request.getParentId();

        // 최상위 카테고리로 이동 (NPE 방지)
        if (requestParentId == null) {
            return;
        }

        // 부모 id가 본인 id 인 경우
        if (requestParentId.equals(request.getId())) {
            throw new GlobalException(ErrorCode.INVALID_PARENT_SELF);
        }

        // 부모 id가 존재하지 않는 id 인 경우
        if (!categoryRepository.existsById(requestParentId)) {
            throw new GlobalException(ErrorCode.INVALID_PARENT);
        }
    }

    // 이름 중복 검사
    public void validateName(String requestName) {

        if (categoryRepository.existsByName(requestName)) {
            throw new GlobalException(ErrorCode.DUPLICATE_CATEGORY);
        }
    }
}
