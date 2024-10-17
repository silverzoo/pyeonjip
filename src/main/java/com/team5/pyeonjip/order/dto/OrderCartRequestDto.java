package com.team5.pyeonjip.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCartRequestDto {

    private Long userId;

    private Long cartTotalPrice; // 쿠폰 적용 후 가격 totalPrice

    private List<OrderDetailDto> orderDetails; // 상품명, 상품 수량, 상품 1개 가격, 상품 이미지
}
