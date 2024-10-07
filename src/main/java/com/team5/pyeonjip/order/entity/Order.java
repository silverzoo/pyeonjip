package com.team5.pyeonjip.order.entity;

import com.team5.pyeonjip.global.entity.BaseTimeEntity;
import com.team5.pyeonjip.order.enums.OrderStatus;
import com.team5.pyeonjip.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient", nullable = false)
    private String recipient; // 수령인

    @Column(name = "phone_num", nullable = false, length = 11)
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

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;  // 주문 상세 리스트

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;  // 배송 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 유저

    // == 주문 수정 메서드 == //
    public void updateOrder(String recipient, String phoneNum, String requirement, Integer deliveryPrice, Long totalPrice) {
        this.recipient = recipient;
        this.phoneNum = phoneNum;
        this.requirement = requirement;
        this.deliveryPrice = deliveryPrice;
        this.totalPrice = totalPrice;
    }
}
