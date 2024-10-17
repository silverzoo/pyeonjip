package com.team5.pyeonjip.category.utils;

import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.entity.Category;
import com.team5.pyeonjip.category.mapper.CategoryMapper;
import com.team5.pyeonjip.category.repository.CategoryRepository;
import com.team5.pyeonjip.global.exception.ErrorCode;
import com.team5.pyeonjip.global.exception.GlobalException;
import com.team5.pyeonjip.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryUtils {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private  final ProductRepository productRepository;

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

    // 최상위 카테고리만 조회
    public List<Category> getParentCategories(List<Category> allCategories) {

        return allCategories.stream()
                .filter(category -> category.getParentId() == null)
                .toList();
    }

    // 부모-자식 카테고리 연결
    public List<CategoryResponse> createChildrenCategories(List<Category> parentCategories,
                                                           List<Category> allCategories) {

        return parentCategories.stream()
                .map(parent -> {
                    List<CategoryResponse> children = allCategories.stream()
                            .filter(child -> parent.getId().equals(child.getParentId()))
                            .map(categoryMapper::toResponse)
                            .toList();

                    // 부모 카테고리의 정보를 가진 CategoryResponse 객체를 생성
                    return categoryMapper.toResponse(parent).toBuilder()
                            .children(children)
                            .build();
                })
                .toList();
    }

    // 부모카테고리 유효성 검사
    public void validateParent(Long id, CategoryRequest request) {

        Long requestParentId = request.getParentId();

        // 최상위 카테고리로 이동 (NPE 방지)
        if (requestParentId == null) {
            return;
        }

        // 부모 id가 본인 id 인 경우
        if (requestParentId.equals(id)) {
            throw new GlobalException(ErrorCode.INVALID_PARENT_SELF);
        }

        // 부모 id가 존재하지 않는 id 인 경우
        if (!categoryRepository.existsById(requestParentId)) {
            throw new GlobalException(ErrorCode.INVALID_PARENT);
        }
    }

    // sort 변경으로 인한 형제 카테고리 sort 업데이트
    public void updateSiblingSort(Category old, CategoryRequest request) {

        // 요청한 순서값이 없으면 재배치 하지 않아도 됨
        if (request.getSort() == null) {
            return;
        }

        List<Category> oldSiblings = categoryRepository.findByParentId(old.getParentId());
        List<Category> newSiblings = categoryRepository.findByParentId(request.getParentId());
        Integer oldSort = old.getSort(), newSort = request.getSort();

        // 형제 카테고리가 변하지 않는다면 해당 뎁스에서만 업데이트, 변한다면 양쪽 뎁스 모두 업데이트
        if (old.getParentId().equals(request.getParentId())) {

            // 요청 sort 값이 현재 sort 값보다 클 경우
            if (newSort > oldSort) {

                newSiblings.stream()
                        .filter(category -> category.getSort() > oldSort && category.getSort() <= newSort)
                        .forEach(category -> {
                            categoryRepository.save(category.toBuilder()
                                    .sort(category.getSort() - 1)
                                    .build());
                        });

            // 요청 sort 값이 현재 sort 값보다 작을 경우
            } else if (newSort < oldSort) {

                newSiblings.stream()
                        .filter(category -> category.getSort() >= newSort && category.getSort() < oldSort)
                        .forEach(category -> {
                            categoryRepository.save(category.toBuilder()
                                    .sort(category.getSort() + 1)
                                    .build());
                        });
            }

        } else {

            //기존 형제 카테고리는 사라진 sort 번호를 채워줘야 함
            oldSiblings.stream()
                    .filter(category -> category.getSort() > oldSort)
                    .forEach(category -> {
                        categoryRepository.save(category.toBuilder()
                                .sort(category.getSort()-1)
                                .build());
                    });

            //새 형제 카테고리는 요청 sort 번호를 비워줘야 함
            newSiblings.stream()
                    .filter(category -> category.getSort() >= newSort)
                    .forEach(category -> {
                        categoryRepository.save(category.toBuilder()
                                .sort(category.getSort() + 1)
                                .build());
                    });
        }
    }

    // 카테고리 삭제 후, 연관된 프로덕트에 null 적용
    public void deleteCategoriesAndUpdateProducts(List<Long> ids) {

        List<Category> categories = validateAndFindCategory(ids);

        categories.forEach(category -> {
            productRepository.findByCategoryId(category.getId()).forEach(product -> {
                product.setCategory(null);
                productRepository.save(product);
            });
        });

        categoryRepository.deleteAll(ids);
    }

    // 이름 중복 검사
    public void validateName(String requestName) {

        if (categoryRepository.existsByName(requestName)) {
            throw new GlobalException(ErrorCode.DUPLICATE_CATEGORY);
        }
    }
}