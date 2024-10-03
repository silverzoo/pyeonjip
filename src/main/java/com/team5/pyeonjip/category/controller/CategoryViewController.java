package com.team5.pyeonjip.category.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class CategoryViewController {

    @GetMapping()
    public String adminHome() {
        return "admin/home";
    }

    @GetMapping ("/category")
    public String adminCategory() {
        return "admin/category";
    }

    @GetMapping ("/order")
    public String adminOrder() {
        return "admin/order";
    }
}
