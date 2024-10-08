package com.team5.pyeonjip.order.service;

import com.team5.pyeonjip.order.dto.*;
import com.team5.pyeonjip.order.entity.Order;
import com.team5.pyeonjip.user.entity.User;

import java.util.List;

public interface OrderService {

    // 주문 생성
    OrderResponseDto createOrder(OrderRequestDto orderDto, DeliveryRequestDto deliveryDto, User user, List<OrderDetailDto> orderDetailDto);

    // 사용자 이름으로 주문 내역 검색
    List<Order> findAllOrdersByUserName(String userName);

    // 주문 수정
    void updateOrder(Long id, OrderUpdateRequestDto requestDto);

    // 주문 삭제
    void deleteOrderById(Long id);

    // 주문 전체 조회
    List<Order> findAllOrders();

    // 주문 상세 조회
    Order findOrderDetail(Long id);

    // 주문 취소
    void cancelOrder(Long id);
}
