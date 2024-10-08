package com.team5.pyeonjip.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Options {

    private String optionName;
    private Long optionPrice;
    private Long maxQuantity;
    private String url;
}
