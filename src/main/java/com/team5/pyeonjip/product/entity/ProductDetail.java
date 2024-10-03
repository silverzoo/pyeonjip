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

}
