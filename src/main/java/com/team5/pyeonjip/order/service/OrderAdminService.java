package com.team5.pyeonjip.order.service;

import com.team5.pyeonjip.order.dto.OrderResponseDto;

import java.util.List;

public interface OrderAdminService {
    // 사용자 이름으로 주문 내역 검색
    List<OrderResponseDto> findOrdersByUserName(String userName);

    // 주문 수정
    void updateDeliveryStatus(Long id, String deliveryStatus);

    // 주문 삭제
    void deleteOrderDetailById(Long orderId);

    // 주문 전체 조회
    List<OrderResponseDto> findAllOrders();
}
