package com.team5.pyeonjip.order.service;

import com.team5.pyeonjip.order.dto.OrderResponseDto;
import com.team5.pyeonjip.order.enums.DeliveryStatus;

import java.util.List;

public interface AdminOrderService {
    // 사용자 이메일로 주문 내역 검색
    List<OrderResponseDto> findOrdersByUserEmail(String userEmail);

    // 주문 수정
    void updateDeliveryStatus(Long id, DeliveryStatus deliveryStatus);

    // 주문 삭제
    void deleteOrderById(Long orderId);

    // 주문 전체 조회
    List<OrderResponseDto> findAllOrders();
}
