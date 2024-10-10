package com.team5.pyeonjip.order.service;

import com.team5.pyeonjip.global.exception.ResourceNotFoundException;
import com.team5.pyeonjip.order.dto.OrderResponseDto;
import com.team5.pyeonjip.order.entity.Delivery;
import com.team5.pyeonjip.order.entity.Order;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import com.team5.pyeonjip.order.mapper.OrderMapper;
import com.team5.pyeonjip.order.repository.DeliveryRepository;
import com.team5.pyeonjip.order.repository.OrderDetailRepository;
import com.team5.pyeonjip.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderAdminServiceImpl implements OrderAdminService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final DeliveryRepository deliveryRepository;

    private boolean isExistOrder(Long orderId) {
        return orderDetailRepository.existsById(orderId);
    }

    // 주문 수정
    @Transactional
    @Override
    public void updateDeliveryStatus(Long id, String deliveryStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("주문을 찾을 수 없습니다."));

        // 배송 상태 변경
        order.getDelivery().updateStatus(DeliveryStatus.valueOf(deliveryStatus));
    }

    // 주문 삭제
    @Transactional
    @Override
    public void deleteOrderDetailById(Long orderId) {
        if(isExistOrder(orderId)) orderDetailRepository.deleteById(orderId);
    }

    // 관리자 - 주문 전체 조회
    @Transactional(readOnly = true)
    @Override
    public List<OrderResponseDto> findAllOrders() {
        return orderRepository.findAll().stream().map(OrderResponseDto::from).toList();
    }

    // 관리자 - 사용자 이름으로 주문 조회
    @Transactional(readOnly = true)
    @Override
    public List<OrderResponseDto> findOrdersByUserName(String userName) {
        // TODO: userRepository 에 userName 에 대한 user 존재여부 확인하는 메소드 추가 후 예외 처리

        return orderRepository.findOrdersByUserName(userName).stream()
                .map(OrderResponseDto::from).toList();
    }
}
