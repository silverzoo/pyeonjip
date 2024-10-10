package com.team5.pyeonjip.cart.service;

import com.team5.pyeonjip.cart.dto.CartDto;
import com.team5.pyeonjip.cart.entity.Cart;
import com.team5.pyeonjip.cart.repository.CartRepository;
import com.team5.pyeonjip.global.exception.ResourceNotFoundException;
import com.team5.pyeonjip.product.dto.ProductResponse;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.product.repository.ProductDetailRepository;
import com.team5.pyeonjip.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    // 추후 팀원과 협의해서 리펙토링 할 예정
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final ProductDetailRepository productDetailRepository;

    public CartDto getProduct(Long productDetailId, Long userId) {

        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("[ProductDetail not found, productDetailId : " + productDetailId + "]"));
        ProductResponse product = productService.getProductById(productDetail.getProduct().getId());
        CartDto dto = new CartDto();

        dto.setUserId(userId);
        dto.setOptionId(productDetailId);
        dto.setName(product.getName());
        dto.setOptionName(productDetail.getName());
        dto.setPrice(productDetail.getPrice());
        dto.setQuantity(1L);
        dto.setMaxQuantity(productDetail.getQuantity());
        dto.setUrl(productDetail.getMainImage());

        return dto;
    }

    public Cart getCartByUserId(Long userId) {
        Cart target = cartRepository.findByUserId(userId);
        return target;
    }

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }


    @Transactional
    public void deleteCartItem(Long userId, Long optionId) {
        cartRepository.deleteByUserIdAndOptionId(userId, optionId);
    }

    public boolean existsByUserIdAndOptionId(Long userId, Long optionId) {
        return cartRepository.existsByUserIdAndOptionId(userId, optionId);
    }
}
