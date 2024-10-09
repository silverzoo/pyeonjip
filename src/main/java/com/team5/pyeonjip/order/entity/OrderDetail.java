package com.team5.pyeonjip.order.entity;

import com.team5.pyeonjip.global.entity.BaseTimeEntity;
import com.team5.pyeonjip.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(name = "quantity", nullable = false)
    @Comment(value = "수량")
    private Long quantity; // 주문 수량

    @Column(name = "product_name", nullable = false)
    @Comment(value = "주문 상품 명")
    private String productName;

    @Column(name = "product_price", nullable = false)
    @Comment(value = "주문 상품 가격")
    private Long productPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "Product_id", referencedColumnName = "id")
//    private Product product;

    // 생성 메서드
    @Builder
    public OrderDetail(Long quantity, String productName, Long productPrice, String imageUrl, Order order){
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
        this.order = order;
    }

    // == 비즈니스 로직 == //

    // 주문 취소 시 상품 재고 더해줌
    public void cancel(){
        // getProduct().addStock(quantity);
    }

    // 주문 상품 전체 가격 조회
    public Long getTotalPrice(){
        return getProductPrice() * getQuantity();
    }
}