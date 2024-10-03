package com.team5.pyeonjip.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class SecurityMainController {

    @GetMapping("/")
    public String mainP() {

        return "Main Controller";
    }
}
