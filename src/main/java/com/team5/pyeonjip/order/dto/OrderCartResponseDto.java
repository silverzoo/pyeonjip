package com.team5.pyeonjip.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OrderCartResponseDto {
    private Long cartTotalPrice; // 장바구니 총 금액
    private Long totalPrice;  // 최종 금액 (장바구니 총 금액 + 배송비 + 등급 할인)
    private Long deliveryPrice; // 배송비
    private double discountRate; // 할인율
    private List<OrderDetailDto> orderDetail; // 상품명, 상품 수량, 상품 가격
}
