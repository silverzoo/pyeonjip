package com.team5.pyeonjip.category.controller;


import com.team5.pyeonjip.category.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryService categoryService;
}
