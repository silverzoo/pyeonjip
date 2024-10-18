package com.team5.pyeonjip.order.controller;

import com.team5.pyeonjip.order.dto.AdminOrderResponseDto;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import com.team5.pyeonjip.order.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<AdminOrderResponseDto>> getOrders(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortField", defaultValue = "createdAt") String sortField, // 기본 최신 순
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(value = "keyword", required = false) String keyword) {

        Page<AdminOrderResponseDto> orders = orderService.findAllOrders(page, size, sortField, sortDir, keyword);
        return ResponseEntity.ok(orders);
    }

    // 관리자 - 주문 수정
    @PatchMapping("orders/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateDeliveryStatus(
            @PathVariable("orderId") Long orderId,
            @RequestParam("deliveryStatus") DeliveryStatus deliveryStatus) {

        orderService.updateDeliveryStatus(orderId, deliveryStatus); // 배송 상태 변경 처리
        return ResponseEntity.ok().build();
    }

    // 관리자 - 주문 삭제
    // TODO : 복수 삭제, soft delete (/orders/ 지워야 할 데이터를 바디에 보내는 것)
    @DeleteMapping("orders/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable("orderId") Long orderId) {
        orderService.deleteOrderById(orderId);
        return ResponseEntity.ok().build();
    }
}