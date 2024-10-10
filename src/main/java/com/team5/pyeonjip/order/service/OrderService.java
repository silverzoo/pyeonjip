package com.team5.pyeonjip.order.service;

import com.team5.pyeonjip.order.dto.*;
import com.team5.pyeonjip.order.entity.Order;

import java.util.List;

public interface OrderService {

    // 주문 생성
    void createOrder(OrderRequestDto orderRequestDto, Long userId);

    // 사용자 id로 주문 전체 조회
    List<OrderResponseDto> findOrdersByUserId(Long userId);

    // 주문 상세 조회
    Order findOrderDetail(Long id);

    // 주문 취소
    void cancelOrder(Long orderId);
}
