package com.team5.pyeonjip.category.service;

import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.entity.Category;
import com.team5.pyeonjip.category.mapper.CategoryMapper;
import com.team5.pyeonjip.category.repository.CategoryRepository;
import com.team5.pyeonjip.global.exception.InvalidParentException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    @DisplayName("조회 성공")
    void getSuccess() {

        // given
        List<Category> categories = Arrays.asList(
                new Category(1L, "침대", 1, null, List.of()),
                new Category(2L, "소파", 2, null, List.of())
        );

        when(categoryRepository.findAll()).thenReturn(categories);

        // when
        List<CategoryResponse> result = categoryService.newGetCategories();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("침대", result.get(0).getName());
        assertEquals("소파", result.get(1).getName());

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("수정 실패: 유효하지 않은 부모 ID로 수정")
    void updateFailWithInvalidId() {

        // given
        Long id = 1L;
        Long newParentId = id;
        String newName = "침대2";
        int newSort = 100;

        Category category = new Category(id, "침대", 1, null, List.of());
        CategoryRequest request = new CategoryRequest(id, newName, newSort, newParentId);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        // when & then
        assertThatThrownBy(() -> categoryService.updateCategory(id, request))
                .isInstanceOf(InvalidParentException.class)
                .hasMessage("해당 카테고리를 상위 카테고리로 수정할 수 없습니다.");

        verify(categoryRepository, times(1)).findById(id);

    }

    @Test
    @DisplayName("수정 실패: 본인을 부모 ID로 수정")
    void updateFailSetSelfAsParentId() {

        // given
        Long id = 1L;
        String newName = "침대2";
        int newSort = 100;

        Category category = new Category(id, "침대", 1, null, List.of());
        CategoryRequest request = new CategoryRequest(id, newName, newSort, category.getId());

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        // when & then
        assertThatThrownBy(() -> categoryService.updateCategory(id, request))
                .isInstanceOf(InvalidParentException.class)
                .hasMessage("해당 카테고리를 상위 카테고리로 수정할 수 없습니다.");

        verify(categoryRepository, times(1)).findById(id);

    }

    @Test
    @DisplayName("수정 성공")
    public void updateSuccess() {

        // given
        Long id = 1L;
        Long newParentId = 2L;
        String newName = "침대2";
        int newSort = 100;

        Category category = new Category(id, "침대", 1, null, List.of());
        CategoryRequest request = new CategoryRequest(id, newName, newSort, newParentId);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.existsById(newParentId)).thenReturn(true);

        // when
        CategoryResponse result = categoryService.updateCategory(id, request);

        // then
        assertNotNull(result);
        assertEquals(newName, result.getName());
        assertEquals(newParentId, result.getParentId());
        assertEquals(newSort, result.getSort());

        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
}