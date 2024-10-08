package com.team5.pyeonjip.cart.service;

import com.team5.pyeonjip.cart.entity.Cart;
import com.team5.pyeonjip.cart.entity.CartItem;
import com.team5.pyeonjip.cart.repository.CartItemRepository;
import com.team5.pyeonjip.cart.repository.CartRepository;
import com.team5.pyeonjip.product.entity.Product;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.product.entity.ProductImage;
import com.team5.pyeonjip.product.repository.ProductDetailRepository;
import com.team5.pyeonjip.product.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductImageRepository productImageRepository;

   public Cart getCartByUserId(Long userId) {
       Cart target = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
       return target;
   }

   public Cart saveCart(Cart cart){
       return cartRepository.save(cart);
   }

   public void clearCart(Long userId) {
       cartRepository.deleteByUserId(userId);
   }
}
