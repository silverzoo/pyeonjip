package com.team5.pyeonjip.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class SecurityAdminController {

    // 매핑 중복으로 /admin -> /admin-test
    @GetMapping("/admin-test")
    public String adminP() {

        return "Admin Controller";
    }
}
