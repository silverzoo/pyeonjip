package com.team5.pyeonjip.order.service;

import com.team5.pyeonjip.global.exception.ResourceNotFoundException;
import com.team5.pyeonjip.order.dto.AdminOrderResponseDto;
import com.team5.pyeonjip.order.entity.Order;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import com.team5.pyeonjip.order.mapper.OrderMapper;
import com.team5.pyeonjip.order.repository.OrderRepository;
import com.team5.pyeonjip.user.entity.User;
import com.team5.pyeonjip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // 주문 수정
    @Transactional
    @Override
    public void updateDeliveryStatus(Long id, DeliveryStatus deliveryStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("주문을 찾을 수 없습니다."));
        // 배송 상태 변경
        order.getDelivery().updateStatus(deliveryStatus);

        orderRepository.save(order);
    }

    // 주문 삭제
    @Transactional
    @Override
    public void deleteOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("주문을 찾을 수 없습니다."));

        orderRepository.delete(order);
    }

    // 관리자 - 주문 전체 조회
    @Transactional(readOnly = true)
    @Override
    public List<AdminOrderResponseDto> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toAdminOrderResponseDto)
                .toList();
    }

    // 관리자 - 사용자 이메일로 주문 조회
    @Transactional(readOnly = true)
    @Override
    public List<AdminOrderResponseDto> findOrdersByUserEmail(String userEmail) {
        // user 존재여부 확인
        User user = userRepository.findByEmail(userEmail);
                //                 .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        return orderRepository.findOrdersByUserEmail(user.getEmail()).stream()
                .map(OrderMapper::toAdminOrderResponseDto)
                .toList();
    }
}