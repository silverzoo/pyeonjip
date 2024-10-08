package com.team5.pyeonjip.product.service;

import com.team5.pyeonjip.global.exception.ResourceNotFoundException;
import com.team5.pyeonjip.product.dto.ProductRequest;
import com.team5.pyeonjip.product.entity.Product;
import com.team5.pyeonjip.product.entity.ProductImage;
import com.team5.pyeonjip.product.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepository productImageRepository;

    // Read - 특정 상품의 모든 이미지 조회
    public List<ProductImage> getProductImagesByProduct(Product product) {
        return productImageRepository.findByProductId(product.getId());
    }

    // Create - 특정 상품에 대한 이미지 생성
    @Transactional
    public void createProductImages(Product product, List<ProductRequest.ProductImageRequest> imageRequests) {
        List<ProductImage> productImages = imageRequests.stream()
                .map(imageRequest -> new ProductImage(product, imageRequest.getImageUrl()))
                .collect(Collectors.toList());
        productImageRepository.saveAll(productImages);
    }

    // Delete - 단일 이미지 삭제
    @Transactional
    public void deleteProductImageById(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 이미지를 찾을 수 없습니다: " + imageId));
        productImageRepository.delete(image);
    }
}