package com.team5.pyeonjip.order.entity;

import com.team5.pyeonjip.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private Long quantity; // 수량

    @Column(name = "product_name", nullable = false)
    private String productName; // 상품 명

    @Column(name = "product_price", nullable = false)
    private Long productPrice; // 상품 가격

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;  // 주문

    // 생성 메서드
    @Builder
    public OrderDetail(Long quantity, String productName, Long productPrice, String imageUrl, Order order){
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
        this.order = order;
    }
}