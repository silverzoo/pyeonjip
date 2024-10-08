package com.team5.pyeonjip.cart.entity;

import com.team5.pyeonjip.product.entity.Product;
import com.team5.pyeonjip.product.entity.ProductDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // 사용자 식별

    private Long productId;

    private Long optionId;

    private Long quantity;


}
