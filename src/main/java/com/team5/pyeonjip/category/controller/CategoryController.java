package com.team5.pyeonjip.category.controller;


import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {

        List<CategoryResponse> categories = categoryService.getCategories();

        return ResponseEntity
                .status(categories.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)
                .body(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,
                                                           @RequestBody CategoryRequest request) {

        CategoryResponse category = categoryService.updateCategory(id, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(category);
    }
}
