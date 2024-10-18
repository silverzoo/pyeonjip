package com.team5.pyeonjip.order.service;

import com.team5.pyeonjip.order.dto.*;
import com.team5.pyeonjip.user.entity.User;

import java.util.List;

public interface OrderService {

    // 주문 생성
    void createOrder(CombinedOrderDto combinedOrderDto, String userEmail);

    // 주문 전체 조회
    List<OrderResponseDto> findOrdersByUserId(Long userId);

    // 주문 취소
    void cancelOrder(Long orderId);

    OrderCartResponseDto getOrderSummary(OrderCartRequestDto orderCartRequestDto);
}