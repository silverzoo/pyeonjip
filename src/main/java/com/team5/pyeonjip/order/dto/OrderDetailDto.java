package com.team5.pyeonjip.order.dto;

import lombok.Builder;
import lombok.Getter;

// 주문 상세 요청
@Getter
@Builder
public class OrderDetailDto {
    private Long productDetailId;
    private String productName;
    private Long quantity;
    private Long productPrice; // 상품 개별 가격
}
