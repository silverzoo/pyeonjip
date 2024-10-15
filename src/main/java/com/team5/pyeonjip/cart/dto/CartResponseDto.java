package com.team5.pyeonjip.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

// for Checkout
public class CartResponseDto {
    private Long discountedTotalPrice;
    private Long userId;
    private List<CartDetailDto> cartDetail;
}
