package com.team5.pyeonjip.order.mapper;

import com.team5.pyeonjip.order.dto.AdminOrderResponseDto;
import com.team5.pyeonjip.order.dto.OrderDetailDto;
import com.team5.pyeonjip.order.dto.OrderRequestDto;
import com.team5.pyeonjip.order.dto.OrderResponseDto;
import com.team5.pyeonjip.order.entity.Delivery;
import com.team5.pyeonjip.order.entity.Order;
import com.team5.pyeonjip.order.entity.OrderDetail;
import com.team5.pyeonjip.order.enums.OrderStatus;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.user.entity.User;

public class OrderMapper {

    // entity -> dto
    public static OrderResponseDto toOrderResponseDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .orderStatus(order.getStatus())
                .deliveryStatus(order.getDelivery().getStatus())
                .createdAt(order.getCreatedAt())
                .orderDetails(order.getOrderDetails().stream()
                        .map(detail -> OrderDetailDto.builder()
                                .productName(detail.getProductName())
                                .productPrice(detail.getTotalPrice())
                                .quantity(detail.getQuantity())
                                .build())
                        .toList())
                .build();
    }

    // entity -> dto
    public static AdminOrderResponseDto toAdminOrderResponseDto(Order order) {
        return AdminOrderResponseDto.builder()
                .id(order.getId())
                .userName(order.getUser().getName())
                .phoneNumber(order.getUser().getPhoneNumber())
                .orderStatus(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .deliveryStatus(order.getDelivery().getStatus())
                .orderDetails(order.getOrderDetails().stream()
                        .map(detail -> OrderDetailDto.builder()
                                .productDetailId(detail.getId())
                                .productName(detail.getProductName())
                                .productPrice(detail.getTotalPrice())
                                .quantity(detail.getQuantity())
                                .build())
                        .toList())
                .build();
    }

    // dto -> entity
    public static Order toOrderEntity(OrderRequestDto orderRequestDto, Delivery delivery, User user, Long totalPrice) {
        return Order.builder()
                .recipient(orderRequestDto.getRecipient() != null ? orderRequestDto.getRecipient() : user.getName()) // 수령인, 유저 기본 이름 사용
                .phoneNumber(orderRequestDto.getPhoneNumber() != null ? orderRequestDto.getPhoneNumber() : user.getPhoneNumber())   // 연락처, 유저 기본 연락처 사용
                .requirement(orderRequestDto.getRequirement()) // 요청사항은 입력받은 값 사용
                .status(OrderStatus.ORDER)                     // 주문 상태는 기본적으로 ORDER로 설정
                .delivery(delivery)     // 배송 정보 저장
                .totalPrice(totalPrice)
                .user(user)
                .build();
    }

    // dto -> entity
    public static OrderDetail toOrderDetailEntity(Order order, ProductDetail product, OrderDetailDto orderDetailDto) {
        return OrderDetail.builder()
                .product(product)
                .order(order)
                .productName(orderDetailDto.getProductName())
                .quantity(orderDetailDto.getQuantity())
                .productPrice(orderDetailDto.getProductPrice())
                .build();
    }
}