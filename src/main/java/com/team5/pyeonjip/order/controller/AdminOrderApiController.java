package com.team5.pyeonjip.order.controller;

import com.team5.pyeonjip.order.dto.OrderResponseDto;
import com.team5.pyeonjip.order.service.OrderAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api")
public class AdminOrderApiController {

    private final OrderAdminService orderService;

    // 관리자 - 주문 전체 조회
    @GetMapping("/order/list")
    public List<OrderResponseDto> getAllOrders(){
        // 전체 주문 목록 조회
        return orderService.findAllOrders();
    }

    // 관리자 - 특정 주문 조회
    @GetMapping("/order")
    public List<OrderResponseDto> getOrdersByUserName(@RequestParam("userName") String userName) {
        return orderService.findOrdersByUserName(userName);
    }

    // 관리자 - 주문 수정
    @PatchMapping("order/{orderId}")
    public ResponseEntity<Void> updateDeliveryStatus(
            @PathVariable("orderId") Long orderId,
            @RequestParam("deliveryStatus") String deliveryStatus) {

        // 배송 상태 변경 처리
        orderService.updateDeliveryStatus(orderId, deliveryStatus);
        return ResponseEntity.ok().build();
    }

    // 관리자 - 주문 삭제
    @DeleteMapping("order/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("orderId") Long orderId) {
        // 주문 삭제 처리
        orderService.deleteOrderDetailById(orderId);
        return ResponseEntity.ok().build();
    }
}
