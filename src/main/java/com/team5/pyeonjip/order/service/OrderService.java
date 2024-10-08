package com.team5.pyeonjip.order.service;

import com.team5.pyeonjip.order.dto.*;
import com.team5.pyeonjip.order.entity.Order;
import com.team5.pyeonjip.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {

    // 주문 생성
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto, User user);

    // 사용자 이름으로 주문 내역 검색
    List<Order> findAllOrdersByUserName(String userName);

    // 주문 수정
    OrderResponseDto updateOrder(Long id, OrderUpdateRequestDto requestDto);

    // 주문 삭제
    void deleteOrderById(Long id);

    // 주문 전체 조회
    List<Order> findAllOrders();

    // 사용자 id로 주문 전체 조회
    List<OrderResponseDto> findOrdersByUser(Long userId);

    // 주문 상세 조회
    Order findOrderDetail(Long id);

    // 주문 취소
    void cancelOrder(Long id);
}
