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
    private String userEmail; // 주문자 이메일
    private String userName; // 주문자 명
    private String phoneNumber; // 연락처
    private OrderStatus orderStatus; // 기본 ORDER
    private Long totalPrice; // 결제 금액 = 상품 가격 * 상품 수량

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createdAt; // 생성 일
    private DeliveryStatus deliveryStatus; // 배송 상태
    private List<OrderDetailDto> orderDetails; // 상품 명, 상품 수량
}