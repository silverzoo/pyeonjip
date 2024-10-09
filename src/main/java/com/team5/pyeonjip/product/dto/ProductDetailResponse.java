package com.team5.pyeonjip.product.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductDetailResponse {
    private Long id;        // ProductDetail ID
    private Long productId; // 연관된 Product ID
    private String name;    // 옵션 이름 (예: 색깔-사이즈)
    private Long price;     // 가격
    private Long quantity;  // 재고 수량
    private String mainImage;  // 대표 이미지 URL 필드 추가
}