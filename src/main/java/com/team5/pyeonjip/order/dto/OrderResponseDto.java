package com.team5.pyeonjip.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import com.team5.pyeonjip.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

// 사용자 - 주문 응답
@Getter
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private Long id; // 주문 id
    private OrderStatus orderStatus; // 주문 상태

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Timestamp createdAt; // 주문 일자
    private DeliveryStatus deliveryStatus; // 배송 상태
    private List<OrderDetailDto> orderDetails; // 상품 명, 상품 수량, 상품*수량 금액
    // 상품 이미지 추가?
}
