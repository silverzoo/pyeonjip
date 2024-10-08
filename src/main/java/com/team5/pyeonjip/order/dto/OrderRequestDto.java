package com.team5.pyeonjip.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

// 주문 요청
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {

    @NotBlank(message = "수령인은 필수 입력 항목입니다.")
    private String recipient;

    @NotBlank(message = "연락처는 필수 입력 항목입니다.")
    @Size(min = 11, message = "연락처는 - 없이 11자리여야 합니다.")
    private String phoneNum; // 유효성 검사

    private String requirement;

    private List<OrderDetailDto> orderDetails;

    @NotBlank(message = "배송지는 필수 입력 항목입니다.")
    private DeliveryRequestDto delivery;
}