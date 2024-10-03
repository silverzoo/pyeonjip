package com.team5.pyeonjip.order.entity;

import com.team5.pyeonjip.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity; // 수량

    @Column(name = "product_name", nullable = false)
    private String productName; // 상품 명

    @Column(name = "product_price", nullable = false)
    private String productPrice; // 상품 가격

    @Column(name = "image_url", nullable = false)
    private String imageUrl; // 상품 이미지 경로
}
