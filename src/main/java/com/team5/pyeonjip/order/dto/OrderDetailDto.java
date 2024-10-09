package com.team5.pyeonjip.order.dto;

import com.team5.pyeonjip.order.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 주문 상세 요청
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDto {
    private Long productId;
    private String productName;
    private Long quantity;
    private Long productPrice; // 상품 개별 가격

    public static OrderDetailDto from(OrderDetail orderDetail) {
        return new OrderDetailDto(orderDetail.getProduct().getId(), orderDetail.getProductName(), orderDetail.getQuantity(), orderDetail.getProductPrice());
    }
}
