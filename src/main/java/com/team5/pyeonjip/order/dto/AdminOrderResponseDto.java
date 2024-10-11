package com.team5.pyeonjip.order.dto;

import com.team5.pyeonjip.order.entity.Order;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import com.team5.pyeonjip.order.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

// 관리자 - 주문 응답
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminOrderResponseDto {
    private Long id; // 주문 id
    private String userName; // 구매자
    private String phoneNumber; // 연락처
    private OrderStatus orderStatus; // 기본 ORDER
    private Long totalPrice;
    private LocalDateTime createdAt;
    private DeliveryStatus deliveryStatus;
    private List<OrderDetailDto> orderDetails; // 상품 명, 상품 수량

    public static AdminOrderResponseDto from(Order order) {
        return new AdminOrderResponseDto(order.getId(),
                order.getUser().getName(), order.getUser().getPhoneNumber(), order.getStatus(),
                order.getTotalPrice(), order.getCreatedAt().toLocalDateTime(), order.getDelivery().getStatus(),
                order.getOrderDetails().stream().map(OrderDetailDto::from).toList());
    }
}