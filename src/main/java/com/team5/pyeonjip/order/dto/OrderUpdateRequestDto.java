package com.team5.pyeonjip.order.dto;

import com.team5.pyeonjip.order.enums.DeliveryStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderUpdateRequestDto {
    private DeliveryStatus deliveryStatus;  // 배송 상태
}
