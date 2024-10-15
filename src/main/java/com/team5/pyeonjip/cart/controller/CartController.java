package com.team5.pyeonjip.cart.controller;

import com.team5.pyeonjip.cart.dto.CartDetailDto;
import com.team5.pyeonjip.cart.dto.CartDto;
import com.team5.pyeonjip.cart.dto.CartResponseDto;
import com.team5.pyeonjip.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CartService cartService;

    // 장바구니 페이지
    @GetMapping
    public ResponseEntity<Void> cart(){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 로그인 동기화 (서버 -> 로컬)
    @PostMapping("/sync")
    public ResponseEntity<List<CartDto>> syncCart(@RequestParam("optionId") Long userId){
            return ResponseEntity.ok(cartService.getCartItemsByUserId(userId));
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
    @PostMapping("/cart-items")
    public ResponseEntity<CartDto> addCart(@RequestBody CartDto cartDto, @RequestParam("userId") Long userId) {
           return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addCartDto(cartDto,userId));
    }

    // 조회
    @GetMapping("/cart-items")
    public ResponseEntity<List<CartDetailDto>> getCartItems(@RequestParam("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.mapCartDtosToCartDetails(cartService.getCartItemsByUserId(userId)));
    }

    // 수정
    @PutMapping("/cart-items/{optionId}")
    public ResponseEntity<CartDto> updateCartItemQuantity(
            @RequestParam("userId") Long userId,
            @PathVariable("optionId") Long optionId,
            @RequestBody CartDto cartDto) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.updateCartItemQuantity(userId, optionId, cartDto));
    }

    // 개별 삭제
    @DeleteMapping("/cart-items/{optionId}")
    public ResponseEntity<Void> deleteCartItem(
            @RequestParam("userId") Long userId,
            @PathVariable("optionId") Long optionId) {
            cartService.deleteCartItemByUserIdAndOptionId(userId,optionId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 전체 삭제
    @DeleteMapping("/cart-items")
    public ResponseEntity<Void> deleteAllCartItems(@RequestParam("userId") Long userId) {
            cartService.deleteAllCartItems(userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/checkout")
    public ResponseEntity<CartResponseDto> checkout(@RequestParam("userId") Long userId,
                                                    @RequestParam("discountedTotalPrice") Long discountedTotalPrice) {
        CartResponseDto responseDtos = new CartResponseDto();
        List<CartDetailDto> detailDtos = cartService.mapCartDtosToCartDetails(cartService.getCartItemsByUserId(userId));

        responseDtos.setCartDetail(detailDtos);
        responseDtos.setUserId(userId);
        responseDtos.setDiscountedTotalPrice(discountedTotalPrice);

        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }

    // WIP
    private Long getAuthenticatedUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

