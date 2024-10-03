package com.team5.pyeonjip.order.entity;

import com.team5.pyeonjip.global.entity.BaseTimeEntity;
import com.team5.pyeonjip.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient", nullable = false)
    private String recipient; // 수령인

    @Column(name = "phone_num", nullable = false)
    private String phoneNum; // 전화번호

    @Column(name = "requirement")
    private String requirement; // 주문 시 요청사항

    @Column(name = "total_price", nullable = false)
    private Long totalPrice; // 총금액

    @Column(name = "delivery_price", nullable = false)
    private Integer deliveryPrice; // 배송비

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status; // 주문 상태
}
