package com.team5.pyeonjip.cart.controller;

import com.team5.pyeonjip.cart.dto.CartItemResponseDTO;
import com.team5.pyeonjip.cart.entity.Cart;
import com.team5.pyeonjip.cart.service.CartService;
import com.team5.pyeonjip.coupon.entity.Coupon;
import com.team5.pyeonjip.coupon.repository.CouponRepository;
import com.team5.pyeonjip.product.entity.Product;
import com.team5.pyeonjip.product.repository.ProductRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CouponRepository couponRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;



    // 테스트 샌드박스용 페이지
    @GetMapping("/sandbox")
    public List<CartItemResponseDTO> sandbox() {
        List<CartItemResponseDTO> target = new ArrayList<>();
        CartItemResponseDTO dto1 = cartService.getProduct(1L,1L);
        CartItemResponseDTO dto2 = cartService.getProduct( 2L,1L);
        CartItemResponseDTO dto3 = cartService.getProduct( 3L,1L);
        CartItemResponseDTO dto4 = cartService.getProduct( 4L,1L);
        CartItemResponseDTO dto5 = cartService.getProduct( 5L,1L);
        CartItemResponseDTO dto6 = cartService.getProduct( 6L,1L);
        CartItemResponseDTO dto7 = cartService.getProduct( 7L,1L);
        CartItemResponseDTO dto8 = cartService.getProduct( 8L,1L);

        target.add(dto1);
        target.add(dto2);
        target.add(dto3);
        target.add(dto4);
        target.add(dto5);
        target.add(dto6);
        target.add(dto7);
        target.add(dto8);

        return target;
    }

    // 장바구니 페이지
    @GetMapping
    public List<Coupon> cart(){
        List<Coupon> coupons = couponRepository.findAll();

        return coupons;
    }

    @GetMapping("/sync")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable Long userId){
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }


    @PostMapping("/save")
    public ResponseEntity<Cart> saveCart(@RequestBody Cart cart) {
        Cart savedCart = cartService.saveCart(cart);
        return ResponseEntity.ok(savedCart);
    }
}

