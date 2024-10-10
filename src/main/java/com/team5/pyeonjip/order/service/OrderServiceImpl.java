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
import com.team5.pyeonjip.order.repository.OrderDetailRepository;
import com.team5.pyeonjip.order.repository.OrderRepository;
import com.team5.pyeonjip.product.entity.Product;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.product.repository.ProductDetailRepository;
import com.team5.pyeonjip.product.repository.ProductRepository;
import com.team5.pyeonjip.user.entity.Grade;
import com.team5.pyeonjip.user.entity.User;
import com.team5.pyeonjip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductDetailRepository productRepository;

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("유저를 찾을 수 없습니다."));
    }

    private Delivery createDelivery(String address) {
        return Delivery.builder()
                .address(address)
                .status(DeliveryStatus.READY) // 초기 배송 상태
                .build();
    }

    private ProductDetail findProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("상품이 존재하지 않습니다."));
    }

    // 주문 생성
    @Transactional
    @Override
    public void createOrder(OrderRequestDto orderRequestDto, Long userId) {

        // 유저 조회
        User user = getUserById(userId);

        // 배송 정보 생성
        Delivery delivery = createDelivery(orderRequestDto.getAddress());
        deliveryRepository.save(delivery);

        Long cartTotalPrice = 100000L; // 장바구니 쿠폰 적용 후 가격 예시

        Long totalPrice = calculateTotalPrice(user, cartTotalPrice);

        // 주문 생성
        Order order = OrderMapper.toEntity(orderRequestDto, delivery, user, totalPrice);
        orderRepository.save(order);

        // 주문 상세 정보 생성
        orderRequestDto.getOrderDetails().forEach(orderDetailDto ->
                orderDetailRepository.save(OrderMapper.toEntity(order,
                        findProductById(orderDetailDto.getProductId()), orderDetailDto)));
    }

    // 회원 등급에 따른 배송비 계산
    private Long calculateDeliveryPrice(User user) {
        // 기본 배송비 3000원
        Long deliveryPrice = 3000L;

        if (user.getGrade().equals(Grade.GOLD)) {
            deliveryPrice = 0L;
        }
        return deliveryPrice;
    }

    // 회원 등급에 따른 할인율 계산
    private double calculateDiscountRate(User user) {
        double discountRate = 0.0;

        switch (user.getGrade()) {
            case GOLD:
                discountRate = 0.1; // 10% 할인
                break;
            case SILVER:
                discountRate = 0.05; // 5% 할인
                break;
            case BRONZE:
                discountRate = 0.0; // 할인 없음
                break;
        }
        return discountRate;
    }

    // 총 금액 계산
    private Long calculateTotalPrice(User user, Long cartTotalPrice) {

        // 1. 회원 등급에 따른 할인율 계산
        double discountRate = calculateDiscountRate(user);

        // 2. 회원 등급에 따른 배송비 계산
        Long deliveryPrice = calculateDeliveryPrice(user);

        // 3. 장바구니 총 금액에서 할인율 적용
        Long discountedPrice = Math.round(cartTotalPrice * (1 - discountRate));

        // 4. 배송비를 적용한 최종 금액 계산
        Long totalPrice = discountedPrice + deliveryPrice;

        return totalPrice;
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderResponseDto> findOrdersByUserId(Long userId) {
        // 사용자 ID로 주문 목록 조회
        List<Order> orders = orderRepository.findOrdersByUserId(userId);

        // 주문 목록 -> OrderResponseDto로 변환
        return orders.stream()
                .map(OrderMapper::toDto)
                .toList();
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
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("주문을 찾을 수 없습니다."));

        // 배송 상태가 READY인 경우에만 취소 가능
        if (order.getDelivery().getStatus() != DeliveryStatus.READY) {
            throw new IllegalStateException("배송이 시작된 주문은 취소할 수 없습니다.");
        }

        order.updateStatus(OrderStatus.CANCEL);

        order.getOrderDetails().forEach(orderDetail ->
                productRepository.findById(orderDetail.getProduct().getId()).ifPresentOrElse(
                    productDetail -> {}, // TODO: {} 안에서 productDetail.updateQuantity() 호출
                        () -> new ResourceNotFoundException("상품이 존재하지 않습니다.")
                ));
    }

    private boolean isExistUser(Long userId) {
        return userRepository.existsById(userId);
    }
}