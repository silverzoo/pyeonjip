package com.team5.pyeonjip.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("chat")
public class ChatViewController {

    @GetMapping
    public String chatPage(){
        return "chat/chat";
    }
}
