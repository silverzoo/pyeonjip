package com.team5.pyeonjip.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; //옵션 이름(색깔-사이즈)

    private Long price;

    private  Long quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // 필요한 필드만 사용하는 커스텀 생성자 추가
    public ProductDetail(Product product, String name, Long price, Long quantity) {
        this.product = product;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

}
