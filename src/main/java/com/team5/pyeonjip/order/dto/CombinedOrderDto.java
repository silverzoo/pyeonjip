package com.team5.pyeonjip.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CombinedOrderDto {
    private OrderRequestDto orderRequestDto;
    private OrderCartRequestDto orderCartRequestDto;
    private String userEmail;
}