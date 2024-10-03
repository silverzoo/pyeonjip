package com.team5.pyeonjip.product.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductResponse {
    private Long id;  // Product ID
    private String name;
    private String description;
    private List<ProductDetailResponse> productDetails; // 상세 정보 목록
    private List<ProductImageResponse> productImages;   // 이미지 정보 목록

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ProductDetailResponse {
        private Long id;       // ProductDetail ID
        private String name;   // 옵션 이름 (예: 색깔-사이즈)
        private Long price;    // 가격
        private Long quantity; // 수량
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ProductImageResponse {
        private Long id;       // ProductImage ID
        private String imageUrl; // 이미지 URL
    }
}