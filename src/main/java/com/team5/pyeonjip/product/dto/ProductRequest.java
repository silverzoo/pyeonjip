package com.team5.pyeonjip.product.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductRequest {
    private String name;
    private String description;
    private Long categoryId;
    private List<ProductDetailRequest> productDetails; // 상세 정보 목록
    private List<ProductImageRequest> productImages;   // 이미지 정보 목록

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ProductDetailRequest {
        private String name;   // 옵션 이름 (예: 색깔-사이즈)
        private Long price;    // 가격
        private Long quantity; // 수량
        private String mainImage;  // 대표 이미지 URL 필드 추가
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ProductImageRequest {
        private String imageUrl; // 이미지 URL
    }
}