package com.team5.pyeonjip.user.controller;

import com.team5.pyeonjip.user.dto.SignUpDto;
import com.team5.pyeonjip.user.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
@ResponseBody
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/signup")
    public String signUpProcess(SignUpDto dto) {

        signUpService.signUpProcess(dto);

        return "ok";
    }
}
