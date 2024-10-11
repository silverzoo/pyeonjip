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
    private final CartService cartService;

    // 장바구니 페이지
        @GetMapping
    public ResponseEntity<Void> cart(){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        // 페이지 로드용이니까 void 반환가능하지 않을까?
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
}

