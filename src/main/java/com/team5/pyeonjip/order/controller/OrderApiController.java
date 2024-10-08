package com.team5.pyeonjip.order.controller;

import com.team5.pyeonjip.order.dto.OrderRequestDto;
import com.team5.pyeonjip.order.dto.OrderResponseDto;
import com.team5.pyeonjip.order.service.OrderService;
import com.team5.pyeonjip.user.entity.User;
import com.team5.pyeonjip.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderApiController {

    private final OrderService orderService;
    private final UserService userService;

    // 사용자 - 주문 생성
    @PostMapping("/order/create")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto){
        User user = new User(); // 로그인한 사용자 정보 가져오기 test

        // 주문 생성 처리
        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto, user);

        return ResponseEntity.ok(orderResponseDto);
    }

    // 사용자 - 주문 목록 조회
    @GetMapping("/mypage/order/list")
    public ResponseEntity<List<OrderResponseDto>> getUserOrders() {
        User user = new User(); // 로그인한 사용자 정보 가져오기 test

        // 사용자별 주문 목록 조회
        List<OrderResponseDto> orderList = orderService.findOrdersByUser(user.getId());

        return ResponseEntity.ok(orderList);
    }

    // 사용자 - 주문 취소

}
