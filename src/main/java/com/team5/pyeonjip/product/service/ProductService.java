package com.team5.pyeonjip.product.service;

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
        // 1. Product 엔티티 생성 및 저장
        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productRepository.save(product);

        // 2. 옵션 및 이미지 생성 및 저장 (각 서비스에 위임)
        productDetailService.createProductDetails(savedProduct, productRequest.getProductDetails());
        productImageService.createProductImages(savedProduct, productRequest.getProductImages());

        // 3. 최종적으로 DTO로 변환 후 반환
        return productMapper.toDto(savedProduct, savedProduct.getProductDetails(), savedProduct.getProductImages());
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("product not found with id: " + id));
        return productMapper.toDto(product, product.getProductDetails(), product.getProductImages());
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        // 1. 기존 상품 정보 조회 및 업데이트
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("product not found with id: " + id));
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());


        return productMapper.toDto(product, product.getProductDetails(), product.getProductImages());
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("product not found with id: " + id));
        productRepository.delete(product);
    }
}