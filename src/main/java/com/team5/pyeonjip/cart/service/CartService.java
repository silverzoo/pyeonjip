package com.team5.pyeonjip.cart.service;

import com.team5.pyeonjip.cart.dto.CartItemResponseDTO;
import com.team5.pyeonjip.cart.entity.Cart;
import com.team5.pyeonjip.cart.repository.CartRepository;
import com.team5.pyeonjip.global.exception.CartNotFoundException;
import com.team5.pyeonjip.product.dto.ProductResponse;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.product.entity.ProductImage;
import com.team5.pyeonjip.product.repository.ProductDetailRepository;
import com.team5.pyeonjip.product.repository.ProductImageRepository;
import com.team5.pyeonjip.product.service.ProductService;
import com.team5.pyeonjip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    // 추후 팀원과 협의해서 리펙토링 할 예정
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductImageRepository productImageRepository;

   public Cart getCartByUserId(Long userId) {
       Cart target = cartRepository.findByUserId(userId).orElseThrow(() -> new CartNotFoundException("[Cart not found, userId : " + userId + "]"));
       return target;
   }

   public Cart saveCart(Cart cart){
       return cartRepository.save(cart);
   }

   public void clearCart(Long userId) {
       cartRepository.deleteByUserId(userId);
   }


   public CartItemResponseDTO getProduct(Long productDetailId, Long userId) {

       ProductDetail productDetail = productDetailRepository.findById(productDetailId).orElseThrow(() -> new CartNotFoundException("[ProductDetail not found, productDetailId : " + productDetailId + "]"));
       ProductResponse product = productService.getProductById(productDetail.getProduct().getId());
       ProductImage productImage = productImageRepository.findById(productDetailId).orElseThrow(() -> new CartNotFoundException("[ProductImage not found, productDetailId : " + productDetailId + "]"));
       CartItemResponseDTO dto = new CartItemResponseDTO();

       dto.setUserId(userId);
       //dto.setProductId(product.getId());
       dto.setOptionId(productDetailId);
       dto.setName(product.getName());
       dto.setOptionName(productDetail.getName());
       dto.setPrice(productDetail.getPrice());
       dto.setQuantity(1L);
       dto.setMaxQuantity(productDetail.getQuantity());
       dto.setUrl(productImage.getImageUrl());

       return dto;
   }
}
