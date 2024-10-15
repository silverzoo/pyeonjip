package com.team5.pyeonjip.order.service;

import com.team5.pyeonjip.global.exception.ErrorCode;
import com.team5.pyeonjip.global.exception.GlobalException;
import com.team5.pyeonjip.global.exception.ResourceNotFoundException;
import com.team5.pyeonjip.order.dto.*;
import com.team5.pyeonjip.order.entity.Delivery;
import com.team5.pyeonjip.order.entity.Order;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import com.team5.pyeonjip.order.enums.OrderStatus;
import com.team5.pyeonjip.order.mapper.OrderMapper;
import com.team5.pyeonjip.order.repository.DeliveryRepository;
import com.team5.pyeonjip.order.repository.OrderDetailRepository;
import com.team5.pyeonjip.order.repository.OrderRepository;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.product.repository.ProductDetailRepository;
import com.team5.pyeonjip.product.service.ProductDetailService;
import com.team5.pyeonjip.user.entity.Grade;
import com.team5.pyeonjip.user.entity.User;
import com.team5.pyeonjip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductDetailService productDetailService;

    private ProductDetail findProductDetailById(Long productId) { // 주문을 동시에 하게 되면, 재고 이상으로 주문이 될 수 있다.
        return productDetailRepository.findById(productId)
                .orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));
    }

    // 주문 생성
    @Transactional
    @Override
    public void createOrder(OrderRequestDto orderRequestDto, Long userId) {

        // TODO: 유저 조회 -> 로그인 된 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("유저를 찾을 수 없습니다."));

        // 배송 정보 생성
        Delivery delivery = Delivery.builder()
                .address(orderRequestDto.getAddress())  // 전달된 주소 사용
                .status(DeliveryStatus.READY)  // 기본 상태를 READY로 설정
                .build();
        deliveryRepository.save(delivery);

        Long cartTotalPrice = 100000L; // 장바구니 쿠폰 적용 후 가격 예시

        // TODO: 장바구니의 각 상품 주문에 추가

        // 전체 주문 금액
        Long totalPrice = calculateTotalPrice(user, cartTotalPrice);

        // 주문 생성
        Order order = OrderMapper.toOrderEntity(orderRequestDto, delivery, user, totalPrice);
        orderRepository.save(order);

        // 주문 상세 정보 생성
        orderRequestDto.getOrderDetails().forEach(orderDetailDto -> {
            ProductDetail productDetail = findProductDetailById(orderDetailDto.getProductDetailId());

            // 재고 수량 확인
            if (productDetail.getQuantity() < orderDetailDto.getQuantity()) {
                throw new GlobalException(ErrorCode.OUT_OF_STOCK);
            }
            orderDetailRepository.save(OrderMapper.toOrderDetailEntity(order, productDetail, orderDetailDto));

            // 주문 후 재고 수량 감소
            productDetail.setQuantity(productDetail.getQuantity() - orderDetailDto.getQuantity());
            productDetailRepository.save(productDetail);
        });
    }

    // 회원 등급에 따른 배송비 계산
    private Long calculateDeliveryPrice(User user) {
        // 기본 배송비 3000원
        long deliveryPrice = 3000L;

        if (user.getGrade().equals(Grade.GOLD)) {
            deliveryPrice = 0L;
        }
        return deliveryPrice;
    }

    // 회원 등급에 따른 할인율 계산
    private double calculateDiscountRate(User user) {
        return switch (user.getGrade()) {
            case GOLD -> 0.1; // 10% 할인
            case SILVER -> 0.05; // 5% 할인
            case BRONZE -> 0.0; // 할인 없음
        };
    }

    // 총 금액 계산
    private Long calculateTotalPrice(User user, Long cartTotalPrice) {
        // 1. 회원 등급에 따른 할인율 계산
        double discountRate = calculateDiscountRate(user);

        // 2. 회원 등급에 따른 배송비 계산
        Long deliveryPrice = calculateDeliveryPrice(user);

        // 3. 최종 금액 계산
        return Math.round(cartTotalPrice * (1 - discountRate)) + deliveryPrice;
    }

    // 주문 조회
    @Transactional(readOnly = true)
    @Override
    public List<OrderResponseDto> findOrdersByUserId(Long userId) {
        // 사용자 ID로 주문 목록 조회
        List<Order> orders = orderRepository.findOrdersByUserId(userId);

        if (orders.isEmpty()) {
            throw new GlobalException(ErrorCode.USER_ORDER_NOT_FOUND);
        }
        // 주문 목록 -> OrderResponseDto로 변환
        return orders.stream()
                .map(OrderMapper::toOrderResponseDto)
                .toList();
    }

    // 주문 취소
    @Transactional
    @Override
    public void cancelOrder(Long orderId) { // User authenticatedUser
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new GlobalException(ErrorCode.ORDER_NOT_FOUND));
        // .filter(order -> order.getUser().getUserId().equals(authenticatedUser.getUserId())) // 주문자 확인

        // 배송 상태가 READY인 경우에만 취소 가능
        if (order.getDelivery().getStatus() != DeliveryStatus.READY) {
            throw new GlobalException(ErrorCode.DELIVERY_ALREADY_STARTED);
        }

        order.updateStatus(OrderStatus.CANCEL);

        // 재고 복구
        order.getOrderDetails().forEach(orderDetail -> {
            ProductDetail productDetail = findProductDetailById(orderDetail.getProduct().getId());
            productDetailService.updateDetailQuantity(productDetail.getId(), orderDetail.getQuantity());
        });
    }
}