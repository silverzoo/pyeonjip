package com.team5.pyeonjip.cart.controller;

import com.team5.pyeonjip.cart.dto.CartDetailDto;
import com.team5.pyeonjip.cart.dto.CartDto;
import com.team5.pyeonjip.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CartService cartService;

    // 로그인 동기화 (로컬 -> 서버)
    @PostMapping("/sync")
    public ResponseEntity<List<CartDto>> sync(@RequestBody List<CartDto> localCartItems, @RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.sync(email, localCartItems));
    }

    // 세부 데이터 가져오기
    @GetMapping("/detail")
    public ResponseEntity<List<CartDetailDto>> getCartDetails(
            @RequestParam("optionId") List<Long> optionId,
            @RequestParam("quantity") List<Long> quantity) {
        List<CartDto> cartDtos =
                optionId.stream().map(id -> new CartDto(id, quantity.get(optionId.indexOf(id)))).toList();
        return ResponseEntity.status(HttpStatus.OK).body(cartService.mapCartDtosToCartDetails(cartDtos));
    }

    // 추가
    @PostMapping
    public ResponseEntity<CartDto> addCart(@RequestBody CartDto cartDto, @RequestParam("email") String email) {
           return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addCartDto(cartDto,email));
    }

    // 조회
    @GetMapping
    public ResponseEntity<List<CartDetailDto>> getCartItems(@RequestParam("email") String email) {
        System.out.println("email : " + email);
        return ResponseEntity.status(HttpStatus.OK).body(cartService.mapCartDtosToCartDetails(cartService.getCartItemsByEmail(email)));
    }

    // 수정
    @PutMapping("/{optionId}")
    public ResponseEntity<CartDto> updateCartItemQuantity(
            @RequestParam("email") String email,
            @PathVariable("optionId") Long optionId,
            @RequestBody CartDto cartDto) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.updateCartItemQuantity(email, optionId, cartDto));
    }

    // 개별 삭제
    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteCartItem(
            @RequestParam("email") String email,
            @PathVariable("optionId") Long optionId) {
            cartService.deleteCartItemByEmailAndOptionId(email,optionId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 전체 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteAllCartItems(@RequestParam("email") String email) {
            cartService.deleteAllCartItems(email);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

