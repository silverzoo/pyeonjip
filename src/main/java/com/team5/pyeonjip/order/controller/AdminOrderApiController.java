package com.team5.pyeonjip.order.controller;

import com.team5.pyeonjip.order.dto.AdminOrderResponseDto;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import com.team5.pyeonjip.order.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
public class AdminOrderApiController {

    private final AdminOrderService orderService;

    // 관리자 - 주문 전체 조회
    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdminOrderResponseDto>> getAllOrders(Pageable pageable){
        // 전체 주문 목록 조회
        Page<AdminOrderResponseDto> orders = orderService.findAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    // 관리자 - 특정 사용자 주문 조회
    @GetMapping("/orders/search")
    @PreAuthorize("hasRole('ADMIN')") // 전체 조회 확장해서 쿼리스트링을 붙여서 하나의 API로 쓰는 경우도 있다.
    public ResponseEntity<Page<AdminOrderResponseDto>> getOrdersByUserEmail(@RequestParam("userEmail") String userEmail, Pageable pageable) {
        // 사용자 이메일로 조회
        Page<AdminOrderResponseDto> orders = orderService.findOrdersByUserEmail(userEmail, pageable);
        return ResponseEntity.ok(orders);
    }

    // 관리자 - 주문 수정
    @PatchMapping("orders/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateDeliveryStatus(
            @PathVariable("orderId") Long orderId,
            @RequestParam("deliveryStatus") DeliveryStatus deliveryStatus) {

        // 배송 상태 변경 처리
        orderService.updateDeliveryStatus(orderId, deliveryStatus);
        return ResponseEntity.ok().build();
    }

    // 관리자 - 주문 삭제
    // TODO : 복수 삭제, soft delete (/orders/ 지워야 할 데이터를 바디에 보내는 것)
    @DeleteMapping("orders/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable("orderId") Long orderId) {
        // 주문 삭제 처리
        orderService.deleteOrderById(orderId);
        return ResponseEntity.ok().build();
    }
}
