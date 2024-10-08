package com.team5.pyeonjip.order.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryRequestDto {
    private String address; // 배송지 직접 입력
}
