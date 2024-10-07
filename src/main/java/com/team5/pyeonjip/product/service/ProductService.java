package com.team5.pyeonjip.product.service;

import com.team5.pyeonjip.product.dto.ProductRequest;
import com.team5.pyeonjip.product.dto.ProductResponse;
import com.team5.pyeonjip.product.entity.Product;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.product.entity.ProductImage;
import com.team5.pyeonjip.product.mapper.ProductMapper;  // 추가된 Mapper 의존성
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
    private final ProductDetailRepository productDetailRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductMapper productMapper;  // Mapper 의존성 추가

    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        // Product 엔티티 생성 및 저장
        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productRepository.save(product);

        // ProductDetail 리스트 생성 및 저장
        List<ProductDetail> productDetails = productRequest.getProductDetails().stream()
                .map(detailRequest -> productMapper.toEntity(detailRequest, savedProduct))
                .collect(Collectors.toList());
        productDetailRepository.saveAll(productDetails);

        // ProductImage 리스트 생성 및 저장
        List<ProductImage> productImages = productRequest.getProductImages().stream()
                .map(imageRequest -> productMapper.toEntity(imageRequest, savedProduct))
                .collect(Collectors.toList());
        productImageRepository.saveAll(productImages);

        // 변환 후 반환
        return productMapper.toDto(savedProduct, productDetails, productImages);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품이 존재하지 않습니다."));
        List<ProductDetail> productDetails = productDetailRepository.findByProductId(product.getId());
        List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());

        return productMapper.toDto(product, productDetails, productImages);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> {
                    List<ProductDetail> productDetails = productDetailRepository.findByProductId(product.getId());
                    List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());
                    return productMapper.toDto(product, productDetails, productImages);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품이 존재하지 않습니다."));
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        productRepository.save(existingProduct);

        // ProductDetail 업데이트
        productDetailRepository.deleteByProductId(id);
        List<ProductDetail> updatedDetails = productRequest.getProductDetails().stream()
                .map(detailRequest -> productMapper.toEntity(detailRequest, existingProduct))
                .collect(Collectors.toList());
        productDetailRepository.saveAll(updatedDetails);

        // ProductImage 업데이트
        productImageRepository.deleteByProductId(id);
        List<ProductImage> updatedImages = productRequest.getProductImages().stream()
                .map(imageRequest -> productMapper.toEntity(imageRequest, existingProduct))
                .collect(Collectors.toList());
        productImageRepository.saveAll(updatedImages);

        return productMapper.toDto(existingProduct, updatedDetails, updatedImages);
    }
}