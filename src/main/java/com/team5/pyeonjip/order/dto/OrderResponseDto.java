package com.team5.pyeonjip.order.dto;

import com.team5.pyeonjip.order.enums.OrderStatus;
import com.team5.pyeonjip.user.entity.Grade;
import lombok.*;

import java.util.List;

// 주문 응답
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private Long id; // 주문 id
    private String recipient; // 기본 수령인
    private String phoneNumber; // 기본 연락처
    private String totalPrice; // 총 금액
    private Long deliveryPrice; // 배송비
    private Long discountRate; // 할인율
    private DeliveryResponseDto delivery; // 기본 배송지
    private Grade grade; // 회원 등급
    private OrderStatus status; // 기본 ORDER
    private List<OrderDetailDto> orderDetails;
}
