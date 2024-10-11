package com.team5.pyeonjip.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartDetailDto {
    //private Long userId;
    private Long optionId;
    private String name;
    private String optionName;
    private Long price;
    private Long quantity;
    private Long maxQuantity;
    private String url;
    private boolean isChecked;
}
