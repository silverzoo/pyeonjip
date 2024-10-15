package com.team5.pyeonjip.order.entity;

import com.team5.pyeonjip.global.entity.BaseTimeEntity;
import com.team5.pyeonjip.product.entity.ProductDetail;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(name = "quantity", nullable = false)
    @Comment(value = "수량")
    private Long quantity; // 상품 수량

    @Column(name = "product_name", nullable = false)
    @Comment(value = "주문 상품 명")
    private String productName;

    @Column(name = "product_price", nullable = false)
    @Comment(value = "주문 상품 가격")
    private Long productPrice; // 1개 가격

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private ProductDetail product;

    @Builder
    public OrderDetail(Long quantity, String productName, Long productPrice, Order order, ProductDetail product){
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
        this.order = order;
        this.product = product;
    }

    // == 비즈니스 로직 == //

    // 수량 * 가격
    public Long getSubTotalPrice(){
        return getProductPrice() * getQuantity();
    }
}