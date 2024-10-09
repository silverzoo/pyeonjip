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
    private String mainImage;  // 대표 이미지 URL 필드 추가
    private List<ProductDetailResponse> productDetails;
    private List<ProductImageResponse> productImages;   // 이미지 정보 목록

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