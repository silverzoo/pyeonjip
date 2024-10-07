package com.team5.pyeonjip.order.service;

import com.team5.pyeonjip.order.dto.DeliveryRequestDto;
import com.team5.pyeonjip.order.dto.OrderRequestDto;
import com.team5.pyeonjip.order.dto.OrderResponseDto;
import com.team5.pyeonjip.order.entity.Order;
import com.team5.pyeonjip.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderService {

    // 주문 생성
    OrderResponseDto createOrder(OrderRequestDto requestDto, DeliveryRequestDto deliveryDto, User user);

    // 주문 수정
    void updateOrderbyId(Long id);

    // 주문 삭제
    void deleteOrderById(Long id);

    // 주문 전체 조회
    List<Order> findAllOrders();

    // 주문 상세 조회
    Order findOrderDetail(Long id);

    // 주문 취소
    // void cancelOrder(Long id);
}
