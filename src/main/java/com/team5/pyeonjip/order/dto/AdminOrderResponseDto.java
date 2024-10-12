package com.team5.pyeonjip.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import com.team5.pyeonjip.order.enums.OrderStatus;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

// 관리자 - 주문 응답
@Getter
@AllArgsConstructor
@Builder
public class AdminOrderResponseDto {
    private Long id; // 주문 id
    private String userName; // 구매자
    private String phoneNumber; // 연락처
    private OrderStatus orderStatus; // 기본 ORDER
    private Long totalPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createdAt;
    private DeliveryStatus deliveryStatus;
    private List<OrderDetailDto> orderDetails; // 상품 명, 상품 수량
}