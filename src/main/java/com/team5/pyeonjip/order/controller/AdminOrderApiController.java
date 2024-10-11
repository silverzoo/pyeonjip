package com.team5.pyeonjip.order.controller;

import com.team5.pyeonjip.order.dto.AdminOrderResponseDto;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import com.team5.pyeonjip.order.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
public class AdminOrderApiController {

    private final AdminOrderService orderService;

    // 관리자 - 주문 전체 조회
    @GetMapping("/orders")
    public ResponseEntity<List<AdminOrderResponseDto>> getAllOrders(){
        // 전체 주문 목록 조회
        List<AdminOrderResponseDto> orders = orderService.findAllOrders();
        return ResponseEntity.ok(orders);
    }

    // 관리자 - 특정 사용자 주문 조회
    @GetMapping("/orders/search")
    public ResponseEntity<List<AdminOrderResponseDto>> getOrdersByUserEmail(@RequestParam("userEmail") String userEmail) {
        // 사용자 이메일로 조회
        List<AdminOrderResponseDto> orders = orderService.findOrdersByUserEmail(userEmail);
        return ResponseEntity.ok(orders);
    }

    // 관리자 - 주문 수정
    @PatchMapping("order/{orderId}")
    public ResponseEntity<Void> updateDeliveryStatus(
            @PathVariable("orderId") Long orderId,
            @RequestParam("deliveryStatus") DeliveryStatus deliveryStatus) {

        // 배송 상태 변경 처리
        orderService.updateDeliveryStatus(orderId, deliveryStatus);
        return ResponseEntity.ok().build();
    }

    // 관리자 - 주문 삭제
    @DeleteMapping("order/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("orderId") Long orderId) {
        // 주문 삭제 처리
        orderService.deleteOrderById(orderId);
        return ResponseEntity.ok().build();
    }
}
