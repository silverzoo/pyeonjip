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
import com.team5.pyeonjip.product.entity.Product;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.product.repository.ProductDetailRepository;
import com.team5.pyeonjip.product.repository.ProductRepository;
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

        Long deliveryPrice = calculateDeliveryPrice(foundUser);

        Long cartTotalPrice = 100000L; // 장바구니 쿠폰 적용 후 가격 예시

        Long totalPrice = calculateTotalPrice(user, cartTotalPrice);

        // 주문 생성
        Order order = Order.builder()
                .recipient(orderRequestDto.getRecipient())
                .phoneNumber(orderRequestDto.getPhoneNumber())
                .requirement(orderRequestDto.getRequirement())
                .delivery(delivery)
                .totalPrice(totalPrice)  // 총 금액
                .deliveryPrice(deliveryPrice)  // 배송비
                .status(OrderStatus.ORDER)  // 주문 상태
                .build();
        orderRepository.save(order);

//        // orderDetails가 null일 경우 빈 리스트로 초기화
//        if (order.getOrderDetails() == null) {
//            order.setOrderDetails(new ArrayList<>());
//        }

        // 주문 상세 정보 생성
        List<OrderDetail> orderDetails = orderRequestDto.getOrderDetails().stream()
                .map(orderDetailDto -> {
                    Product product = productRepository.findById(orderDetailDto.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다."));

                    ProductDetail productDetail = product.getProductDetails().stream()
                            .filter(detail -> detail.getId().equals(orderDetailDto.getProductId())) // ProductDetailId로 필터링
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("상품 세부 정보를 찾을 수 없습니다."));

                    return OrderDetail.builder()
                            .productName(product.getName())  // 상품명
                            .productPrice(productDetail.getPrice())  // 상품 가격
                            .quantity(orderDetailDto.getQuantity())  // 수량
                            .order(order)  // 해당 주문 연결
                            .build();
                })
                .collect(Collectors.toList());

        // 주문 객체에 상세 정보 추가
        orderDetails.forEach(order::addOrderDetail);

        return OrderMapper.toDto(order);
    }

    // 회원 등급에 따른 배송비 계산
    private Long calculateDeliveryPrice(User user) {
        // 기본 배송비 3000원
        Long deliveryPrice = 3000L;

        if (user.getGrade().equals("GOLD")) {
            deliveryPrice = 0L;
        }
        return deliveryPrice;
    }

    // 회원 등급에 따른 할인율 계산
    private double calculateDiscountRate(User user) {
        double discountRate = 0.0;

        switch(user.getGrade()) {
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
        return orderRepository.findAllOrdersByUserName(userName);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderResponseDto> findOrdersByUserId(Long userId) {
        // 사용자 ID로 주문 목록 조회
        List<Order> orders = orderRepository.findOrdersByUserId(userId);

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
    public OrderResponseDto cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("주문을 찾을 수 없습니다."));

        // 배송 상태가 READY인 경우에만 취소 가능
        if (order.getDelivery().getStatus() != DeliveryStatus.READY) {
            throw new IllegalStateException("배송이 시작된 주문은 취소할 수 없습니다.");
        }

        order.updateStatus(OrderStatus.CANCEL);

        for(OrderDetail orderDetail:order.getOrderDetails()){
            orderDetail.cancel();
        }

        orderRepository.save(order);

        return OrderMapper.toDto(order);
     }
}