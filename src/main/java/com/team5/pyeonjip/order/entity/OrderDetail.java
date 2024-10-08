package com.team5.pyeonjip.order.entity;

import com.team5.pyeonjip.global.entity.BaseTimeEntity;
import com.team5.pyeonjip.product.entity.Product;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;  // 주문

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Product_id")
    private Product product;

    // 생성 메서드
    @Builder
    public OrderDetail(Long quantity, String productName, Long productPrice, String imageUrl, Order order, Product product){
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
        this.order = order;
        this.product = product;
    }

    // == 비즈니스 로직 == //

    // 주문 취소
    public void cancel(){
        // getProduct().addStock(quantity);
    }

    // 주문 상품 전체 가격 조회
    public Long getTotalPrice(){
        return getProductPrice() * getQuantity();
    }
}