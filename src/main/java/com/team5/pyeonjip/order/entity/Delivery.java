package com.team5.pyeonjip.order.entity;

import com.team5.pyeonjip.global.entity.BaseTimeEntity;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Delivery extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Comment(value = "배송 상태")
    @Column(name = "status", nullable = false)
    private DeliveryStatus status;

    @Column(name = "address")
    @Comment(value = "배송지")
    private String address;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Builder
    public Delivery(String address, DeliveryStatus status) {
        this.address = address;
        this.status = DeliveryStatus.READY;
    }

    // == 비즈니스 로직 == //

    // 배송 상태 변경 메서드
    public void updateStatus(DeliveryStatus newStatus) {
        this.status = newStatus;
    }
}