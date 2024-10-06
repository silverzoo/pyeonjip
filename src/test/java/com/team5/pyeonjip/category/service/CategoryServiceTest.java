package com.team5.pyeonjip.category.service;

import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.entity.Category;
import com.team5.pyeonjip.category.mapper.CategoryMapper;
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

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("카테고리 조회")
    void getCategories() {

        // given: Repository 가 모킹된 카테고리 리스트를 반환하고, Mapper 가 각 카테고리에 대해 Mocked Response 를 반환하도록 설정
        List<Category> categories = Arrays.asList(
                new Category(1L, "침대", null, List.of()),
                new Category(2L, "소파", null, List.of())
        );

        when(categoryRepository.findByParentIsNull()).thenReturn(categories);
        when(categoryMapper.toResponse(categories.get(0)))
                .thenReturn(new CategoryResponse(1L, "침대", 1, null, List.of()));
        when(categoryMapper.toResponse(categories.get(1)))
                .thenReturn(new CategoryResponse(2L, "소파", 1, null, List.of()));

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

        Category category = new Category(1L, "침대", null, List.of());

        CategoryRequest request = new CategoryRequest();
        request.setName(newName);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        // when
        CategoryResponse updatedCategory = categoryService.updateCategory(id, request);

        // then
        assertThat(updatedCategory)
                .isNotNull()
                .extracting(CategoryResponse::getId, CategoryResponse::getName, CategoryResponse::getParentId)
                .containsExactly(id, newName, null);

    }
}