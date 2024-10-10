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
@RequestMapping("/api/order")
public class OrderApiController {

    private final OrderService orderService;
    private final UserService userService;

    // 사용자 - 주문 생성
    @PostMapping("/create")
    public ResponseEntity<Void> createOrder(
            @RequestBody OrderRequestDto orderRequestDto){
//            @RequestParam(value = "userId", required = true) Long userId

        Long userId = 1L; // 회원 아이디 임시 생성
        // 주문 클릭 시 요청받은 장바구니id를 리스트로 받아 주문 테이블 생성 -> 세션에 임시 저장?

        // 주문 생성 처리
        orderService.createOrder(orderRequestDto, userId);

        return ResponseEntity.ok().build();
    }

    // 사용자 - 주문 목록 조회
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> getUserOrders(@RequestParam("userId") Long userId) {

        User user = userService.findUser(userId);

        // 사용자별 주문 목록 조회
        List<OrderResponseDto> orderList = orderService.findOrdersByUserId(user.getId());

        return ResponseEntity.ok(orderList);
    }

    // 사용자 - 주문 취소
    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable("orderId") Long orderId) {
        // 주문 취소 처리
        orderService.cancelOrder(orderId);
        // 취소된 주문 정보를 반환
        return ResponseEntity.ok().build();
    }
}
