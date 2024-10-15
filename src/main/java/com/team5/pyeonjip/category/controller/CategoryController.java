package com.team5.pyeonjip.category.controller;

import com.team5.pyeonjip.category.dto.CategoryResponse;
import com.team5.pyeonjip.category.entity.Category;
import com.team5.pyeonjip.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories(@RequestParam(required = false) List<Long> categoryIds) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCategories(categoryIds));
    }

    @GetMapping("/{parentId}/leaf")
    public ResponseEntity<List<Long>> getLeafCategories(@PathVariable Long parentId) {
        List<Long> leafCategories = categoryService.getLeafCategoryIds(parentId);
        System.out.println(leafCategories.toString());
        return ResponseEntity.status(HttpStatus.OK).body(leafCategories);
    }

}
