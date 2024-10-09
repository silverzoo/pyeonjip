package com.team5.pyeonjip.order.dto;

import com.team5.pyeonjip.order.enums.OrderStatus;
import com.team5.pyeonjip.user.entity.Grade;
import lombok.*;

import java.util.List;

// 관리자 - 주문 응답
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private Long id; // 주문 id
    private String recipient; // 구매자
    private String phoneNumber; // 연락처
    private OrderStatus status; // 기본 ORDER
    private List<OrderDetailDto> orderDetails; // 상품 명, 상품 수량, 이미지
}