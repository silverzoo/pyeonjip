package com.team5.pyeonjip.category.service;

import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.entity.Category;
import com.team5.pyeonjip.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("카테고리 조회")
    void getCategories() {

        // given
        List<Category> categories = Arrays.asList(
                new Category(1L, "침대", 1, null, List.of()),
                new Category(2L, "소파", 1, null, List.of())
        );

        when(categoryRepository.findByParentIsNull()).thenReturn(categories);

        // when
        List<CategoryResponse> result = categoryService.getCategories();

        // then
        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .extracting("name")
                .containsExactly("침대", "소파");

        verify(categoryRepository, times(1)).findByParentIsNull();
    }

    @Test
    @DisplayName("카테고리 이름 수정")
    void updateCategory() {

        // given
        Long id = 1L;
        String newName = "침대2";

        Category category = new Category(id, "침대", 1,null, List.of());

        CategoryRequest request = new CategoryRequest(id, "침대2", 1, null, List.of());

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        // 모의 리포지토리는 실제 save 메서드가 호출될 때 ID가 설정된 객체를 반환하도록 설정
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category savedCategory = invocation.getArgument(0);
            return savedCategory.toBuilder()
                    .id(id)
                    .build();
        });

        // when
        CategoryResponse updatedCategory = categoryService.updateCategory(id, request);

        // then
        assertThat(updatedCategory)
                .isNotNull()
                .extracting(CategoryResponse::getId, CategoryResponse::getName, CategoryResponse::getParentId)
                .containsExactly(id, newName, null);

        verify(categoryRepository, times(1)).findById(id);

    }
}