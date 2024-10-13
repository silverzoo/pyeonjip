package com.team5.pyeonjip.category.controller;

import com.team5.pyeonjip.category.dto.CategoryRequest;
import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/category")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable("categoryId") Long id,
                                                           @RequestBody CategoryRequest request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.updateCategory(id, request));
    }

    @PostMapping()
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.createCategory(request));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable("categoryId") Long id) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.deleteCategory(id));
    }

    //TODO: 전체 삭제, 부분 삭제용 컨트롤러 필요
}
