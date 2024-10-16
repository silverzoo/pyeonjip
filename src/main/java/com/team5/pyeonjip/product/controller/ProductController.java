package com.team5.pyeonjip.product.controller;

import com.team5.pyeonjip.product.dto.ProductRequest;
import com.team5.pyeonjip.product.dto.ProductResponse;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.product.entity.ProductImage;
import com.team5.pyeonjip.product.service.ProductDetailService;
import com.team5.pyeonjip.product.service.ProductImageService;
import com.team5.pyeonjip.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductDetailService productDetailService;
    private final ProductImageService productImageService;

    // 1. 제품 생성
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.createProduct(productRequest);
        return ResponseEntity.ok(productResponse);
    }

    // 2. 제품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    // 3. 제품 수정
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long productId, @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.updateProduct(productId, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    // 4. 제품 디테일 생성
    @PostMapping("/{productId}/details")
    public ResponseEntity<ProductDetail> createProductDetail(@PathVariable Long productId, @RequestBody ProductDetail productDetail) {
        ProductDetail createdDetail = productDetailService.createProductDetail(productId, productDetail);
        return ResponseEntity.ok(createdDetail);
    }

    // 5. 제품 디테일 삭제
    @DeleteMapping("/details/{detailId}")
    public ResponseEntity<Void> deleteProductDetail(@PathVariable Long productId, @PathVariable Long detailId) {
        productDetailService.deleteProductDetail(productId, detailId);
        return ResponseEntity.noContent().build();
    }

    // 6. 제품 디테일 수정
    @PutMapping("/details/{detailId}")
    public ResponseEntity<ProductDetail> updateProductDetail(@PathVariable Long detailId, @RequestBody ProductDetail productDetail) {
        ProductDetail updatedDetail = productDetailService.updateProductDetail(detailId, productDetail);
        return ResponseEntity.ok(updatedDetail);
    }

    // 7. 제품 이미지 생성
    @PostMapping("/{productId}/images")
    public ResponseEntity<ProductImage> createProductImage(@PathVariable Long productId, @RequestBody ProductImage productImage) {
        ProductImage createdImage = productImageService.createProductImage(productId, productImage);
        return ResponseEntity.ok(createdImage);
    }

    // 8. 제품 이미지 삭제
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable Long productId, @PathVariable Long imageId) {
        productImageService.deleteProductImage(productId, imageId);
        return ResponseEntity.noContent().build();
    }

    // CategoryId로 제품 목록 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductResponse> products = productService.getProductsByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    // ProductId로 단일 상품 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        ProductResponse productResponse = productService.getProductById(productId);
        return ResponseEntity.ok(productResponse);
    }

    // 모든 상품 조회 (컨트롤러)
    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> productResponse = productService.getAllProducts();
        return ResponseEntity.ok(productResponse);
    }

    // 여러 CategoryId 로 제품 목록 조회
    @GetMapping("/categories")
    public ResponseEntity<List<ProductResponse>> getProductsByMultipleCategoryIds(
            @RequestParam List<Long> categoryIds) {
        return ResponseEntity.ok(productService.getProductsByMultipleCategoryIds(categoryIds));
    }

    // ProductId로 옵션 목록 조회
    @GetMapping("/{productId}/details")
    public ResponseEntity<List<ProductDetail>> getProductDetails(@PathVariable Long productId) {
        List<ProductDetail> productDetails = productDetailService.getProductDetailsByProductId(productId);
        return ResponseEntity.ok(productDetails);
    }

    // 특정 옵션(디테일) 조회
    @GetMapping("/details/{detailId}")
    public ResponseEntity<ProductDetail> getProductDetailById(@PathVariable Long detailId) {
        ProductDetail productDetail = productDetailService.getProductDetailById(detailId);
        return ResponseEntity.ok(productDetail);

    }

    // 컨트롤러 페이지 네이션
    @GetMapping("/all-pages")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Page<ProductResponse> productResponse = productService.getAllProductspage(PageRequest.of(page, size));
        return ResponseEntity.ok(productResponse);
    }
}