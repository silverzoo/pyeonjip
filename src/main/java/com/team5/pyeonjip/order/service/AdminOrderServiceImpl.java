package com.team5.pyeonjip.order.service;

import com.team5.pyeonjip.global.exception.ErrorCode;
import com.team5.pyeonjip.global.exception.GlobalException;
import com.team5.pyeonjip.order.dto.AdminOrderResponseDto;
import com.team5.pyeonjip.order.entity.Order;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import com.team5.pyeonjip.order.mapper.OrderMapper;
import com.team5.pyeonjip.order.repository.OrderRepository;
import com.team5.pyeonjip.user.entity.User;
import com.team5.pyeonjip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new GlobalException(ErrorCode.ORDER_NOT_FOUND));
    }

    // 주문 수정
    @Transactional
    @Override
    public void updateDeliveryStatus(Long orderId, DeliveryStatus deliveryStatus) {
        Order order = findOrderById(orderId);
        // 배송 상태 변경
        order.getDelivery().updateStatus(deliveryStatus);
        orderRepository.save(order);
    }

    // 주문 삭제
    @Transactional
    @Override
    public void deleteOrderById(Long orderId) {
        Order order = findOrderById(orderId);
        orderRepository.delete(order); // TODO: soft delete
    }

    // 관리자 - 주문 전체 조회
    @Transactional(readOnly = true)
    @Override
    public Page<AdminOrderResponseDto> findAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(OrderMapper::toAdminOrderResponseDto);
    }

    // 관리자 - 사용자 이메일로 주문 조회
    @Transactional(readOnly = true)
    @Override
    public Page<AdminOrderResponseDto> findOrdersByUserEmail(String userEmail, Pageable pageable) {
        // user 존재여부 확인
        User user = userRepository.findByEmail(userEmail);

        Page<Order> orders = orderRepository.findOrdersByUserEmail(user.getEmail(), pageable);

        return orders.map(OrderMapper::toAdminOrderResponseDto);
    }
}