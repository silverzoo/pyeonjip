package com.team5.pyeonjip.order.dto;

import lombok.*;

import java.util.List;

// 주문 요청
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {

    private String recipient;
    private String phoneNum;
    private String requirement;
    private String address;
    private List<OrderDetailRequestDto> orderDetails;
}
