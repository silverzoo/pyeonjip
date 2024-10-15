package com.team5.pyeonjip.order.controller;

import com.team5.pyeonjip.order.dto.OrderRequestDto;
import com.team5.pyeonjip.order.dto.OrderResponseDto;
import com.team5.pyeonjip.order.service.OrderService;
import com.team5.pyeonjip.user.entity.User;
import com.team5.pyeonjip.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderApiController {

    private final OrderService orderService;
    private final UserService userService;

    // 사용자 - 주문 생성
    @PostMapping("/orders")
    public ResponseEntity<Void> createOrder(
            @Valid @RequestBody OrderRequestDto orderRequestDto, @RequestParam("userId") Long userId){
            // @AuthenticationPrincipal User currentUser) {

        // Long userId = currentUser.getId();

        User user = userService.findUser(userId);

        // 주문 생성 처리
        orderService.createOrder(orderRequestDto, user.getId());

        return ResponseEntity.ok().build();
    }

    // 사용자 - 주문 목록 조회(마이페이지)
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> getUserOrders(@RequestParam("userId") Long userId) { // @AuthenticationPrincipal User currentUser

        User user = userService.findUser(userId);
        // User user = currentUser.getId();

        // 사용자별 주문 목록 조회
        List<OrderResponseDto> orderList = orderService.findOrdersByUserId(user.getId());

        return ResponseEntity.ok(orderList);
    }

    // 사용자 - 주문 취소
    @PatchMapping("orders/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable("orderId") Long orderId, @AuthenticationPrincipal User currentUser) {
        // 주문 취소 처리
        orderService.cancelOrder(orderId);

        return ResponseEntity.ok().build();
    }
}
