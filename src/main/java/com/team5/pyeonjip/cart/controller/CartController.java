package com.team5.pyeonjip.cart.controller;

import com.team5.pyeonjip.cart.dto.CartDto;
import com.team5.pyeonjip.cart.entity.Cart;
import com.team5.pyeonjip.cart.service.CartService;
import com.team5.pyeonjip.coupon.entity.Coupon;
import com.team5.pyeonjip.coupon.repository.CouponRepository;
import com.team5.pyeonjip.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CouponRepository couponRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;



    // 테스트 샌드박스용 페이지
    @GetMapping("/sandbox")
    public List<CartDto> sandbox() {
        List<CartDto> target = new ArrayList<>();
        CartDto dto1 = cartService.getCartDto(1L,1L);
        CartDto dto2 = cartService.getCartDto( 1L,2L);
        CartDto dto3 = cartService.getCartDto( 1L,3L);
        CartDto dto4 = cartService.getCartDto( 1L,4L);
        CartDto dto5 = cartService.getCartDto( 1L,5L);
        CartDto dto6 = cartService.getCartDto( 1L,6L);
        CartDto dto7 = cartService.getCartDto( 1L,7L);
        CartDto dto8 = cartService.getCartDto( 1L,8L);

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

    // 로컬 -> 서버
    @PostMapping("/syncLocal")
    public ResponseEntity<List<CartDto>> syncCart(@RequestBody List<CartDto> localCartItems, @RequestParam Long userId) {
        List<CartDto> dtos = cartService.syncCart(userId, localCartItems);

        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    // 서버 -> 로컬
    @PostMapping("/syncServer")
    public ResponseEntity<List<CartDto>> syncCart(@RequestParam Long userId) {
        List<CartDto> dtos = cartService.getCartItemsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    // 장바구니 추가
    @PostMapping("/save")
    public ResponseEntity<Cart> saveCart(@RequestBody Cart cart) {
        Cart savedCart = cartService.saveCart(cart);
        return ResponseEntity.status(HttpStatus.OK).body(savedCart);
    }

    @PostMapping("/clear")
    public ResponseEntity<Cart> clearCart(@RequestBody Cart cart) {
        cartService.clearCart(cart.getUserId());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCartItem(@RequestParam Long userId, @RequestParam Long optionId) {
        if (!cartService.existsByUserIdAndOptionId(userId, optionId)) {
            log.error("Delete cart item fail");
            return ResponseEntity.notFound().build(); // 항목이 없으면 404 반환
        }
        cartService.deleteCartItem(userId, optionId);
        log.info("Delete cart item success");
        return ResponseEntity.noContent().build();
    }

    // 중복체크 API 검사
    @GetMapping("/checkDuplicate")
    public ResponseEntity<Boolean> checkDuplicate(@RequestParam Long userId, @RequestParam Long optionId) {
        boolean exists = cartService.existsByUserIdAndOptionId(userId, optionId);
        return ResponseEntity.ok(exists);
    }
}

