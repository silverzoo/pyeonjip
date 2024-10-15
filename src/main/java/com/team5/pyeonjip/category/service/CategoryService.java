package com.team5.pyeonjip.category.service;

import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.entity.Category;
import com.team5.pyeonjip.category.mapper.CategoryMapper;
import com.team5.pyeonjip.category.repository.CategoryRepository;
import com.team5.pyeonjip.category.utils.CategoryUtils;
import com.team5.pyeonjip.product.entity.Product;
import com.team5.pyeonjip.product.repository.ProductRepository;
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
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryUtils categoryUtils;

    public List<CategoryResponse> getCategories(List<Long> ids) {

        // id 리스트가 없으면 전체 조회, 있으면 부분 조회
        if (ids == null || ids.isEmpty()) {

            List<Category> allCategories = categoryRepository.findAll();
            List<Category> parentCategories = categoryUtils.getParentCategories(allCategories);

            return categoryUtils.createChildrenCategories(parentCategories, allCategories);

        } else {

            List<Category> categories = categoryUtils.findCategory(ids);

            return categories.stream()
                    .map(categoryMapper::toResponse)
                    .toList();
        }
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        Category category = categoryUtils.findCategory(id);

        categoryUtils.validateParent(id, request);

        Integer newSort = categoryUtils.updateSiblingSort(request);

        Category updatedCategory = category.toBuilder()
                .id(id)
                .name(request.getName() != null ? request.getName() : category.getName())
                .sort(request.getSort() != null ? newSort : category.getSort())
                .parentId(request.getParentId() != null ? request.getParentId() : null)
                .build();

        Category savedCategory = categoryRepository.save(updatedCategory);

        return categoryMapper.toResponse(savedCategory);
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {

        Category category = categoryMapper.toEntity(request);

        Category newCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(newCategory);
    }

    @Transactional
    public Map<String, String> deleteCategories(List<Long> ids) {

        Map<String, String> response = new HashMap<>();

        // id 리스트가 없으면 삭제 x, 있으면 부분 삭제
        if (ids == null || ids.isEmpty()) {

            response.put("message", "삭제할 카테고리가 없습니다.");
            return response;

        } else {


            List<String>  deletedNames = new ArrayList<>();

            List<Category> categories = categoryUtils.findCategory(ids);

            for (Category category : categories) {

                List<Product> products = productRepository.findByCategoryId(category.getId());

                for (Product product : products) {
                    product.setCategory(null);
                    productRepository.save(product);
                }

                categoryRepository.delete(category);

                deletedNames.add(category.getName());
            }

            // 삭제 리스트가 현재 조회되는 전체 카테고리 길이와 일치한다면 전체 삭제 메시지 반환

            String message = String.format("%s 카테고리가 삭제되었습니다.", String.join(", ", deletedNames));

            response.put("message", message);
        }

        return response;
    }

    public List<Long> getLeafCategoryIds(Long parentId) {
        List<Object[]> results = categoryRepository.findLeafCategories(parentId);

        // 쿼리 결과에서 첫 번째 요소인 ID만 추출하여 List<Long>로 매핑
        // Repository 쪽에서 매핑하기 어려워서 서비스단에서 매핑했음
        return results.stream()
                .map(result -> (Long) result[0])
                .toList();
    }
}
