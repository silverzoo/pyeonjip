package com.team5.pyeonjip.product.service;

import com.team5.pyeonjip.product.dto.ProductRequest;
import com.team5.pyeonjip.product.dto.ProductResponse;
import com.team5.pyeonjip.product.entity.Product;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.product.entity.ProductImage;
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

    /**
     * 상품 생성 메서드
     */
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        // Product 객체 생성 및 저장
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        Product savedProduct = productRepository.save(product);

        // ProductDetail 리스트 저장
        List<ProductDetail> productDetails = productRequest.getProductDetails().stream()
                .map(detailRequest -> new ProductDetail(null, detailRequest.getName(), detailRequest.getPrice(), detailRequest.getQuantity(), savedProduct))
                .collect(Collectors.toList());
        productDetailRepository.saveAll(productDetails);

        // ProductImage 리스트 저장
        List<ProductImage> productImages = productRequest.getProductImages().stream()
                .map(imageRequest -> new ProductImage(null, imageRequest.getImageUrl(), savedProduct))
                .collect(Collectors.toList());
        productImageRepository.saveAll(productImages);

        // ProductResponse 생성 및 반환
        return convertToResponse(savedProduct, productDetails, productImages);
    }

    /**
     * 상품 ID로 조회 메서드
     */
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품이 존재하지 않습니다."));
        List<ProductDetail> productDetails = productDetailRepository.findByProductId(product.getId());
        List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());

        return convertToResponse(product, productDetails, productImages);
    }

    /**
     * 모든 상품 조회 메서드
     */
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> {
                    List<ProductDetail> productDetails = productDetailRepository.findByProductId(product.getId());
                    List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());
                    return convertToResponse(product, productDetails, productImages);
                })
                .collect(Collectors.toList());
    }

    /**
     * 상품 수정 메서드
     */
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품이 존재하지 않습니다."));

        // 상품 정보 업데이트
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        productRepository.save(existingProduct);

        // ProductDetail 업데이트 (기존 삭제 후 새로 추가)
        productDetailRepository.deleteByProductId(id);
        List<ProductDetail> updatedDetails = productRequest.getProductDetails().stream()
                .map(detailRequest -> new ProductDetail(null, detailRequest.getName(), detailRequest.getPrice(), detailRequest.getQuantity(), existingProduct))
                .collect(Collectors.toList());
        productDetailRepository.saveAll(updatedDetails);

        // ProductImage 업데이트 (기존 삭제 후 새로 추가)
        productImageRepository.deleteByProductId(id);
        List<ProductImage> updatedImages = productRequest.getProductImages().stream()
                .map(imageRequest -> new ProductImage(null, imageRequest.getImageUrl(), existingProduct))
                .collect(Collectors.toList());
        productImageRepository.saveAll(updatedImages);

        return convertToResponse(existingProduct, updatedDetails, updatedImages);
    }

    /**
     * 상품 삭제 메서드
     */
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품이 존재하지 않습니다."));
        productDetailRepository.deleteByProductId(id);
        productImageRepository.deleteByProductId(id);
        productRepository.delete(product);
    }

    /**
     * Product 및 관련 데이터를 ProductResponse로 변환
     */
    private ProductResponse convertToResponse(Product product, List<ProductDetail> productDetails, List<ProductImage> productImages) {
        List<ProductResponse.ProductDetailResponse> detailResponses = productDetails.stream()
                .map(detail -> new ProductResponse.ProductDetailResponse(
                        detail.getId(),
                        detail.getName(),
                        detail.getPrice(),
                        detail.getQuantity()
                ))
                .collect(Collectors.toList());

        List<ProductResponse.ProductImageResponse> imageResponses = productImages.stream()
                .map(image -> new ProductResponse.ProductImageResponse(
                        image.getId(),
                        image.getImageUrl()
                ))
                .collect(Collectors.toList());

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                detailResponses,
                imageResponses
        );
    }
}