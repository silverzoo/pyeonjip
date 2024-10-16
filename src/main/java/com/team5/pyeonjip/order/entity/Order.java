package com.team5.pyeonjip.order.entity;

import com.team5.pyeonjip.global.entity.BaseTimeEntity;
import com.team5.pyeonjip.order.enums.OrderStatus;
import com.team5.pyeonjip.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(name = "recipient", nullable = false)
    @Comment(value = "수령인")
    private String recipient;

    @Column(name = "phone_number", nullable = false, length = 11)
    @Comment(value = "연락처")
    private String phoneNumber;

    @Column(name = "requirement")
    @Comment(value = "주문 시 요청사항")
    private String requirement;

    @Column(name = "total_price", nullable = false)
    @Comment(value = "주문 시 최종 금액")
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Comment(value = "주문 상태")
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", referencedColumnName = "id")
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // == 비즈니스 로직 == //

    // 주문 시 최종 금액
    public Long getTotalPrice(){
        return orderDetails.stream()
                .mapToLong(detail -> detail.getProductPrice() * detail.getQuantity())
                .sum();
    }

    // 주문 상태 변경 메서드
    public void updateStatus(OrderStatus status) {
        this.status = status;
    }
}