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
    private final ProductDetailRepository productDetailRepository;
    private final ProductImageRepository productImageRepository;
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

    // ProductId로 단일 상품 조회
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        // Product 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_NOT_FOUND));

        // 연관된 ProductDetail 및 ProductImage 조회
        List<ProductDetail> productDetails = productDetailRepository.findByProductId(productId);
        List<ProductImage> productImages = productImageRepository.findByProductId(productId);

        // ProductResponse로 변환하여 반환
        return productMapper.toDto(product, productDetails, productImages);
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
                .map(product -> {
                    // ProductDetail과 ProductImage 리스트를 각각 조회
                    List<ProductDetail> productDetails = productDetailRepository.findByProductId(product.getId());
                    List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());

                    // toDto 메서드에 Product와 함께 연관 엔티티들을 전달
                    return productMapper.toDto(product, productDetails, productImages);
                })
                .collect(Collectors.toList());
    }


    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> {
                    List<ProductDetail> productDetails = productDetailRepository.findByProductId(product.getId());
                    List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());
                    return productMapper.toDto(product, productDetails, productImages);
                })
                .collect(Collectors.toList());

    }

    // 여러 CategoryId로 제품 리스트 조회 후 Flat화
    public List<ProductResponse> getProductsByMultipleCategoryIds(List<Long> categoryIds) {
        return categoryIds.stream()
                .flatMap(categoryId -> getProductsByCategoryId(categoryId).stream())
                .collect(Collectors.toList());
    }


}