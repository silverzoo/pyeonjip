package com.team5.pyeonjip.product.mapper;

import com.team5.pyeonjip.product.dto.ProductDetailResponse;
import com.team5.pyeonjip.product.dto.ProductRequest;
import com.team5.pyeonjip.product.dto.ProductResponse;
import com.team5.pyeonjip.product.entity.Product;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.product.entity.ProductImage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    // ProductRequest DTO -> Product Entity 변환
    public Product toEntity(ProductRequest productRequest) {
        return new Product(null, productRequest.getName(), productRequest.getDescription(), null, null, null);
    }

    // ProductDetailRequest DTO -> ProductDetail Entity 변환
    public ProductDetail toEntity(ProductRequest.ProductDetailRequest detailRequest, Product product) {
        return new ProductDetail(null, detailRequest.getName(), detailRequest.getPrice(), detailRequest.getQuantity(), detailRequest.getMainImage(), product);
    }

    // Product Entity -> ProductResponse DTO 변환
    public ProductResponse toDto(Product product, List<ProductDetail> productDetails, List<ProductImage> productImages) {
        List<ProductResponse.ProductDetailResponse> detailResponses = productDetails.stream()
                .map(detail -> new ProductResponse.ProductDetailResponse(detail.getId(), detail.getName(), detail.getPrice(), detail.getQuantity(), detail.getMainImage()))
                .collect(Collectors.toList());

        List<ProductResponse.ProductImageResponse> imageResponses = productImages.stream()
                .map(image -> new ProductResponse.ProductImageResponse(image.getId(), image.getImageUrl()))
                .collect(Collectors.toList());

        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), detailResponses, imageResponses);
    }
}