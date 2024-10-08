package com.team5.pyeonjip.order.service;

import com.team5.pyeonjip.order.dto.*;
import com.team5.pyeonjip.order.entity.Delivery;
import com.team5.pyeonjip.order.entity.Order;
import com.team5.pyeonjip.order.entity.OrderDetail;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import com.team5.pyeonjip.order.enums.OrderStatus;
import com.team5.pyeonjip.order.mapper.OrderMapper;
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
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;

    // 주문 생성
    @Transactional
    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderDto, DeliveryRequestDto deliveryDto, User user, List<OrderDetailDto> orderDetailDto) {

        // 유저 조회
        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 배송 정보 생성
        Delivery delivery = Delivery.builder()
                .address(orderDto.getDelivery().getAddress())
                .status(DeliveryStatus.READY) // 초기 배송 상태
                .build();

        // 주문 생성
        Order order = Order.builder()
                .recipient(orderDto.getRecipient())
                .phoneNumber(orderDto.getPhoneNum())
                .requirement(orderDto.getRequirement())
                .delivery(delivery)
                .user(foundUser)
                .build();

        // 주문 상세 정보 생성
        List<OrderDetail> orderDetails = orderDetailDto.stream()
                .map(productDto -> OrderDetail.builder()
                        .productName(productDto.getProductName())  // 상품명
                        .productPrice(productDto.getProductPrice())  // 상품 가격
                        .quantity(productDto.getQuantity())  // 수량
                        .order(order)  // 해당 주문과 연관 관계 설정
                        .build())
                .collect(Collectors.toList());

        // 주문 저장
        order.getOrderDetails().addAll(orderDetails);
        orderRepository.save(order);

        return OrderMapper.toDto(order, orderDetails);
    }

    // 관리자 - 주문 수정
    @Transactional
    @Override
    public void updateOrder(Long id, OrderUpdateRequestDto requestDto){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        // 배송 상태 변경
        Delivery delivery = order.getDelivery();
        delivery.updateStatus(requestDto.getDeliveryStatus());

        orderRepository.save(order);
    }

    // 주문 삭제
    @Transactional
    @Override
    public void deleteOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
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

    // 주문 상세 조회
    @Transactional(readOnly = true)
    @Override
    public Order findOrderDetail(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
    }

    // 주문 취소
    @Transactional
    @Override
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        order.cancel();
     }
}