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


    // Todo: 현재 항상 "ok"를 반환하도록 구성되어 있음. 추후 성공 / 실패 결과를 나누어 보여줄 것.
    @PostMapping("/signup")
    public String signUpProcess(SignUpDto dto) {

        signUpService.signUpProcess(dto);

        return "ok";
    }
}
