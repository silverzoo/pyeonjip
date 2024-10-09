package com.team5.pyeonjip.cart.controller;

import com.team5.pyeonjip.cart.dto.CartDto;
import com.team5.pyeonjip.cart.entity.Cart;
import com.team5.pyeonjip.cart.service.CartService;
import com.team5.pyeonjip.coupon.entity.Coupon;
import com.team5.pyeonjip.coupon.repository.CouponRepository;
import com.team5.pyeonjip.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public List<CartDto> sandbox() {
        List<CartDto> target = new ArrayList<>();
        CartDto dto1 = cartService.getProduct(1L,1L);
        CartDto dto2 = cartService.getProduct( 2L,1L);
        CartDto dto3 = cartService.getProduct( 3L,1L);
        CartDto dto4 = cartService.getProduct( 4L,1L);
        CartDto dto5 = cartService.getProduct( 5L,1L);
        CartDto dto6 = cartService.getProduct( 6L,1L);
        CartDto dto7 = cartService.getProduct( 7L,1L);
        CartDto dto8 = cartService.getProduct( 8L,1L);

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

    // 장바구니 추가
    @PostMapping("/save")
    public ResponseEntity<Cart> saveCart(@RequestBody Cart cart) {
        Cart savedCart = cartService.saveCart(cart);
        return ResponseEntity.ok(savedCart);
    }

    @PostMapping("/clear")
    public ResponseEntity<Cart> clearCart(@RequestBody Cart cart) {
        cartService.clearCart(cart.getUserId());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCartItem(@RequestParam Long userId, @RequestParam Long optionId) {
        if (!cartService.existsByUserIdAndOptionId(userId, optionId)) {
            return ResponseEntity.notFound().build(); // 항목이 없으면 404 반환
        }
        cartService.deleteCartItem(userId, optionId);
        return ResponseEntity.noContent().build();
    }

    // 중복체크 API 검사
    @GetMapping("/checkDuplicate")
    public ResponseEntity<Boolean> checkDuplicate(@RequestParam Long userId, @RequestParam Long optionId) {
        boolean exists = cartService.existsByUserIdAndOptionId(userId, optionId);
        return ResponseEntity.ok(exists);
    }
}

