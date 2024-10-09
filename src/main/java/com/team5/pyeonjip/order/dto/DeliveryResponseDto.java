package com.team5.pyeonjip.order.dto;

import com.team5.pyeonjip.order.enums.DeliveryStatus;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryResponseDto {
    private Long deliveryId;
    private DeliveryStatus status;
    private String address;
}
