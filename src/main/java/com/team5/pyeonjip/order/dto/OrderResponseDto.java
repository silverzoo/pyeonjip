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
    private String name;
    private String phoneNum;
    private String totalPrice;
    private Long deliveryPrice;
    private Long discountRate;
    private String address;
    private Grade grade;
    private OrderStatus status; // 기본 ORDER
    private List<OrderDetailDto> orderDetails;
}
