package com.team5.pyeonjip.product.service;

import com.team5.pyeonjip.product.dto.ProductRequest;
import com.team5.pyeonjip.product.entity.Product;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.product.repository.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductDetailService {
    private final ProductDetailRepository productDetailRepository;

    // Create - 옵션 생성
    @Transactional
    public void createProductDetails(Product product, List<ProductRequest.ProductDetailRequest> detailRequests) {
        List<ProductDetail> productDetails = detailRequests.stream()
                .map(detailRequest -> new ProductDetail(product, detailRequest.getName(), detailRequest.getPrice(), detailRequest.getQuantity()))
                .collect(Collectors.toList());
        productDetailRepository.saveAll(productDetails);
    }

    // Read - 상품의 모든 옵션 조회
    public List<ProductDetail> getProductDetailsByProduct(Product product) {
        return productDetailRepository.findByProductId(product.getId());
    }

    // Update - 옵션 업데이트
    @Transactional
    public void updateProductDetails(Product product, List<ProductRequest.ProductDetailRequest> detailRequests) {
        List<ProductDetail> existingDetails = productDetailRepository.findByProductId(product.getId());

        existingDetails.forEach(existing -> {
            detailRequests.stream()
                    .filter(request -> request.getName().equals(existing.getName()))
                    .findFirst()
                    .ifPresent(request -> {
                        existing.setPrice(request.getPrice());
                        existing.setQuantity(request.getQuantity());
                    });
        });

        productDetailRepository.saveAll(existingDetails);
    }

    // Delete - 옵션 삭제
    @Transactional
    public void deleteProductDetailsByProduct(Product product) {
        List<ProductDetail> existingDetails = productDetailRepository.findByProductId(product.getId());
        productDetailRepository.deleteAll(existingDetails);
    }
}