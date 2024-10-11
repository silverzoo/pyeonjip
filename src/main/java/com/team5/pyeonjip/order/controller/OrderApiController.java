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
    public ResponseEntity<Void> createOrder(
            @RequestBody OrderRequestDto orderRequestDto)
       //     @RequestParam("userId") Long userId){
    {
        Long userId = 1L;
        // 주문 생성 처리
        orderService.createOrder(orderRequestDto, userId);

        return ResponseEntity.ok().build();
    }

    // 사용자 - 주문 목록 조회(마이페이지)
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> getUserOrders(@RequestParam("userId") Long userId) {

        User user = userService.findUser(userId);

        // 사용자별 주문 목록 조회
        List<OrderResponseDto> orderList = orderService.findOrdersByUserId(user.getId());

        return ResponseEntity.ok(orderList);
    }

    // 사용자 - 주문 취소
    @PatchMapping("order/cancel/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable("orderId") Long orderId) {
        // 주문 취소 처리
        orderService.cancelOrder(orderId);

        return ResponseEntity.ok().build();
    }
}
