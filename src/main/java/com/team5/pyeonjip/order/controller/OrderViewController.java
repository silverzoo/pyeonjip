package com.team5.pyeonjip.order.controller;

import com.team5.pyeonjip.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OrderViewController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public String orderView() {
        return "/order/order";
    }
}
