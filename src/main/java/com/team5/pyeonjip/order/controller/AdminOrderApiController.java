package com.team5.pyeonjip.order.controller;

import com.team5.pyeonjip.order.dto.OrderResponseDto;
import com.team5.pyeonjip.order.dto.OrderUpdateRequestDto;
import com.team5.pyeonjip.order.entity.Order;
import com.team5.pyeonjip.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api")
public class AdminOrderApiController {

    private final OrderService orderService;

    // 관리자 - 주문 전체 조회
    @GetMapping("/order/list")
    public ResponseEntity<List<Order>> getAllOrders(){
        // 전체 주문 목록 조회
        List<Order> orderList = orderService.findAllOrders();

        return ResponseEntity.ok(orderList);
    }

    // 관리자 - 주문 수정
    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateDeliveryStatus(
            @PathVariable Long orderId,
            @RequestBody OrderUpdateRequestDto orderUpdateRequestDto) {

        // 배송 상태 변경 처리
        OrderResponseDto updatedOrder = orderService.updateOrder(orderId, orderUpdateRequestDto);

        return ResponseEntity.ok(updatedOrder);
    }

    // 관리자 - 주문 삭제


}
