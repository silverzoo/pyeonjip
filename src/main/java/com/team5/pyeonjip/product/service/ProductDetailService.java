package com.team5.pyeonjip.product.service;

import com.team5.pyeonjip.global.exception.ErrorCode;
import com.team5.pyeonjip.global.exception.GlobalException;
import com.team5.pyeonjip.global.exception.ResourceNotFoundException;
import com.team5.pyeonjip.product.dto.ProductRequest;
import com.team5.pyeonjip.product.entity.Product;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.product.repository.ProductDetailRepository;
import com.team5.pyeonjip.product.service.S3BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final S3BucketService s3BucketService;

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

    // Quantity Update - 수량 조절 메서드 추가
    @Transactional
    public void updateDetailQuantity(Long detailId, int quantity) {
        ProductDetail productDetail = productDetailRepository.findById(detailId)
                .orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));
        productDetail.setQuantity(productDetail.getQuantity() + quantity); // 수량 변경
        productDetailRepository.save(productDetail);
    }

    // 대표 이미지 업로드 및 저장
    @Transactional
    public String uploadAndSaveMainImage(Long productDetailId, MultipartFile mainImage) throws IOException {
        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        // S3에 업로드하고 대표 이미지 URL 설정
        String mainImageUrl = s3BucketService.upload(mainImage, "main-images");
        productDetail.setMainImage(mainImageUrl);
        productDetailRepository.save(productDetail);

        return mainImageUrl;
    }
}