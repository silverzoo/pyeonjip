package com.team5.pyeonjip.order.dto;

import lombok.Builder;
import lombok.Getter;

// 주문 상세 요청
@Getter
@Builder
public class OrderDetailRequestDto {
    private Long productDetailId;
    private int quantity;
    private Long productPrice; // 상품 개별 가격
}
