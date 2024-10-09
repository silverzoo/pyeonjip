package com.team5.pyeonjip.order.entity;

import com.team5.pyeonjip.global.entity.BaseTimeEntity;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Delivery extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status; // 배송 상태

    @Column(name = "address")
    private String address; // 배송지

    @OneToOne(mappedBy = "delivery")
    private Order order; // 주문

    // 배송 상태 변경 메서드
    public void updateStatus(DeliveryStatus newStatus) {
        this.status = newStatus;
    }
}
