package com.team5.pyeonjip.category.controller;


import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {

        List<CategoryResponse> categories = categoryService.getCategories();

        return ResponseEntity
                .status(categories.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)
                .body(categories);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long categoryId,
                                                           @RequestBody CategoryRequest request) {

        CategoryResponse category = categoryService.updateCategory(categoryId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(category);
    }
}
