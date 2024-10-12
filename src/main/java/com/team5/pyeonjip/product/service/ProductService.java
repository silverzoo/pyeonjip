package com.team5.pyeonjip.product.service;

import com.team5.pyeonjip.global.exception.ErrorCode;
import com.team5.pyeonjip.global.exception.GlobalException;
import com.team5.pyeonjip.global.exception.ResourceNotFoundException;
import com.team5.pyeonjip.product.dto.ProductRequest;
import com.team5.pyeonjip.product.dto.ProductResponse;
import com.team5.pyeonjip.product.entity.Product;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.product.entity.ProductImage;
import com.team5.pyeonjip.product.mapper.ProductMapper;
import com.team5.pyeonjip.product.repository.ProductDetailRepository;
import com.team5.pyeonjip.product.repository.ProductImageRepository;
import com.team5.pyeonjip.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductDetailService productDetailService;
    private final ProductImageService productImageService;

    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productRepository.save(product);

        // ProductDetail 생성 및 저장
        List<ProductDetail> productDetails = productRequest.getProductDetails().stream()
                .map(detailRequest -> productMapper.toEntity(detailRequest, savedProduct))
                .collect(Collectors.toList());
        savedProduct.setProductDetails(productDetails);

        // ProductImage 생성 및 저장
        productImageService.createProductImages(savedProduct, productRequest.getProductImages());

        return productMapper.toDto(savedProduct, savedProduct.getProductDetails(), savedProduct.getProductImages());
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toDto(product, product.getProductDetails(), product.getProductImages());
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        // 1. 기존 상품 정보 조회 및 업데이트
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());


        return productMapper.toDto(product, product.getProductDetails(), product.getProductImages());
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.delete(product);
    }

    // CategoryId로 제품 리스트 조회
    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
                .map(productMapper::toDto)  // 오버로드된 메서드 사용
                .collect(Collectors.toList());
    }
}