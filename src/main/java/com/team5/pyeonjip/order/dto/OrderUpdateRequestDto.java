package com.team5.pyeonjip.order.dto;

import com.team5.pyeonjip.order.enums.DeliveryStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderUpdateRequestDto {
    private String deliveryStatus;  // 배송 상태
}
