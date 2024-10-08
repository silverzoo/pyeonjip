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

    // 장바구니 조회
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId).orElse(new Cart());
    }

    // 장바구니에 항목 추가
    @Transactional
    public Cart addItemToCart(Long userId, Long productDetailId, Long productImageId, Long quantity) {
        // 사용자 ID로 장바구니 조회, 없으면 새로 생성
        Cart cart = cartRepository.findById(userId).orElse(new Cart());
        cart.setUserId(userId);

        // 상품 옵션조회
        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new IllegalArgumentException("상품 옵션을 찾을 수 없습니다.")); // 추후 변경 예정
        ProductImage productImage = productImageRepository.findById(productImageId)
                .orElseThrow(() -> new IllegalArgumentException("상품 옵션을 찾을 수 없습니다.")); // 추후 변경 예정

        // 장바구니에 동일 상품이 이미 있는지 검토
        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProductDetail().getId().equals(productDetailId))
                .findFirst();
        if(existingCartItem.isPresent()) {
            // 이미 있는 상품이라면 수량만 증가
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        else{
            CartItem cartItem = new CartItem();
            cartItem.setProductDetail(productDetail);
            cartItem.setProductImage(productImage);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cart.getItems().add(cartItem);
        }

        return cartRepository.save(cart);
    }
    // 장바구니 항목 삭제
    @Transactional
    public void removeItemFromCart(Long userId, Long cartItemId){
        Cart cart = cartRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다.")); // 추후 변경 예정
        cart.getItems().removeIf(item -> item.getId().equals(cartItemId));
        cartRepository.save(cart);
    }

    // 장바구니 총 금액 계산
    public Long calculateTotalPrice(Long userId) {
        Cart cart = getCartByUserId(userId);
        return cart.getItems().stream()
                .mapToLong(item -> item.getProductDetail().getPrice() * item.getQuantity()).sum();
    }


}
