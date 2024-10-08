package com.team5.pyeonjip.cart.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CartItemResponseDTO {
    private Long id;
    private String name;

    private List<Options> productDetails;
}
