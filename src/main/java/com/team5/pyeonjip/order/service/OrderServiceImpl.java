package com.team5.pyeonjip.order.service;

import com.team5.pyeonjip.order.dto.*;
import com.team5.pyeonjip.order.entity.Delivery;
import com.team5.pyeonjip.order.entity.Order;
import com.team5.pyeonjip.order.entity.OrderDetail;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import com.team5.pyeonjip.order.mapper.OrderMapper;
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
    public OrderResponseDto createOrder(OrderRequestDto requestDto, DeliveryRequestDto deliveryDto, User user) {

        // 유저 조회
        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 배송 정보 생성
        Delivery delivery = Delivery.builder()
                .address(deliveryDto.getAddress())
                .status(DeliveryStatus.READY)
                .build();

        // 주문 상세 리스트 생성
        List<OrderDetail> orderItems = requestDto.getOrderDetails().stream()
                .map(detailDto -> {
                    // 상품 조회
                    ProductDetail productDetail = productDetailRepository.findById(detailDto.getProductDetailId())
                            .orElseThrow(() -> new IllegalArgumentException("상품 옵션을 찾을 수 없습니다."));

                    Product product = productDetail.getProduct();

                    // image 제외
                    return OrderDetail.builder()
                            .productName(product.getName())
                            .productPrice(productDetail.getPrice())
                            .quantity(productDetail.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());

        // 주문 생성
        Order order = Order.builder()
                .user(foundUser)
                .delivery(delivery)
                .orderDetails(orderItems)
                .build();

        // 주문 저장
        orderRepository.save(order);

        return OrderMapper.toDto(order);
    }

    // 주문 수정
    @Transactional
    @Override
    public void updateOrderbyId(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        // 수정 로직 추가

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

    // 주문 전체 조회
    @Transactional(readOnly = true)
    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    // 주문 상세 조회
    @Transactional(readOnly = true)
    @Override
    public Order findOrderDetail(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
    }

    // 주문 취소
    // @Transactional
}
