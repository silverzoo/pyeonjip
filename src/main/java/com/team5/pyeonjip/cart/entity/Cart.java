package com.team5.pyeonjip.cart.entity;

import com.team5.pyeonjip.product.entity.Product;
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    private List<CartItem> items = new ArrayList<>();

    private Long userId; // 사용자 식별

}
