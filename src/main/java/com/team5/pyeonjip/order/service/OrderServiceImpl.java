package com.team5.pyeonjip.order.service;

import com.team5.pyeonjip.global.exception.ResourceNotFoundException;
import com.team5.pyeonjip.order.dto.*;
import com.team5.pyeonjip.order.entity.Delivery;
import com.team5.pyeonjip.order.entity.Order;
import com.team5.pyeonjip.order.entity.OrderDetail;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import com.team5.pyeonjip.order.enums.OrderStatus;
import com.team5.pyeonjip.order.mapper.OrderMapper;
import com.team5.pyeonjip.order.repository.DeliveryRepository;
import com.team5.pyeonjip.order.repository.OrderRepository;
import com.team5.pyeonjip.product.repository.ProductDetailRepository;
import com.team5.pyeonjip.product.repository.ProductRepository;
import com.team5.pyeonjip.user.entity.User;
import com.team5.pyeonjip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;

    // 주문 생성
    @Transactional
    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, User user) {

        // 유저 조회
        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("유저를 찾을 수 없습니다."));

        // 배송 정보 생성
        Delivery delivery = Delivery.builder()
                .address(orderRequestDto.getDelivery().getAddress())
                .status(DeliveryStatus.READY) // 초기 배송 상태
                .build();
        deliveryRepository.save(delivery);

        // 주문 생성
        Order order = Order.builder()
                .recipient(orderRequestDto.getRecipient())
                .phoneNumber(orderRequestDto.getPhoneNumber())
                .requirement(orderRequestDto.getRequirement())
                .delivery(delivery)
                .user(foundUser)
                .totalPrice(orderRequestDto.getTotalPrice())  // 총 금액
                .deliveryPrice(orderRequestDto.getDeliveryPrice())  // 배송비
                .status(OrderStatus.ORDER)  // 주문 상태
                .build();
        orderRepository.save(order);

        // 주문 상세 정보 생성
        List<OrderDetail> orderDetails = orderRequestDto.getOrderDetails().stream()
                .map(orderDetailDto -> OrderDetail.builder()
                        .productName(orderDetailDto.getProductName())  // 상품명
                        .productPrice(orderDetailDto.getProductPrice())  // 상품 가격
                        .quantity(orderDetailDto.getQuantity())  // 수량
                        .order(order)  // 해당 주문 연결
                        .build())
                .collect(Collectors.toList());
        orderDetails.forEach(order::addOrderDetail);

        return OrderMapper.toDto(order);
    }

    // 관리자 - 주문 수정
    @Transactional
    @Override
    public OrderResponseDto updateOrder(Long id, OrderUpdateRequestDto orderUpdateRequestDto){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("주문을 찾을 수 없습니다."));
        // 배송 상태 변경
        Delivery delivery = order.getDelivery();
        delivery.updateStatus(DeliveryStatus.valueOf(orderUpdateRequestDto.getDeliveryStatus()));

        deliveryRepository.save(delivery);

        return OrderMapper.toDto(order);
    }

    // 주문 삭제
    @Transactional
    @Override
    public void deleteOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("주문을 찾을 수 없습니다."));
        orderRepository.delete(order);
    }

    // 관리자 - 주문 전체 조회
    @Transactional(readOnly = true)
    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    // 관리자 - 사용자 이름으로 주문 조회
    @Transactional(readOnly = true)
    @Override
    public List<Order> findAllOrdersByUserName(String userName) {
        return orderRepository.findAllByUser_Name(userName);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderResponseDto> findOrdersByUser(Long userId) {
        // 사용자 ID로 주문 목록 조회
        List<Order> orders = orderRepository.findAllByUserId(userId);

        // 주문 목록 -> OrderResponseDto로 변환
        return orders.stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    // 주문 상세 조회
    @Transactional(readOnly = true)
    @Override
    public Order findOrderDetail(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("주문을 찾을 수 없습니다."));
    }

    // 주문 취소
    @Transactional
    @Override
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("주문을 찾을 수 없습니다."));
        order.cancel();
     }
}