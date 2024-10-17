package com.team5.pyeonjip.category.controller;

import com.team5.pyeonjip.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class GetCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<?>> getCategories(@RequestParam(required = false) List<Long> categoryIds) {

        List<?> categories;

        // 단일 조회, 상위 id로 자식 카테고리 id 리스트 추출
        if (categoryIds != null && categoryIds.size() == 1) {

            categories = categoryService.getLeafCategoryIds(categoryIds.getFirst());

        // 전체 조회 혹은 부분 조회, id와 일치하는 모든 카테고리 추출
        } else {

            categories = categoryService.getCategories(categoryIds);

        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categories);
    }

}
