package com.team5.pyeonjip.order.controller;

import com.team5.pyeonjip.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api")
public class AdminOrderApiController {

    private final OrderService orderService;

    // 주문 삭제


    // 주문 수정


    // 주문 목록


}
