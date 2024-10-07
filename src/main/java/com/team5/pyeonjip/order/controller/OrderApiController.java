package com.team5.pyeonjip.order.controller;

import com.team5.pyeonjip.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderApiController {

    private final OrderService orderService;

    // 주문 생성 get

    // 주문 처리 post

}
