package com.team5.pyeonjip.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 배송 전 사용자 주문 목록 수정 요청
@Getter
@Setter
@NoArgsConstructor
public class OrderUpdateRequestDto {
    private String recipient;  // 수령인
    private String phoneNum;   // 전화번호
    private String requirement; // 요청 사항
}
