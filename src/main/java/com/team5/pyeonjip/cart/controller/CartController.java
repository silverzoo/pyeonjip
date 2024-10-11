package com.team5.pyeonjip.cart.controller;

import com.team5.pyeonjip.cart.dto.CartDetailDto;
import com.team5.pyeonjip.cart.dto.CartDto;
import com.team5.pyeonjip.cart.service.CartService;
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
    public ResponseEntity<List<CartDetailDto>> syncCart(@RequestBody List<CartDetailDto> localCartItems, @RequestParam Long userId) {
        List<CartDetailDto> dtos = cartService.syncCart(userId, localCartItems);
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    // 서버 -> 로컬
    @PostMapping("/syncServer")
    public ResponseEntity<List<CartDetailDto>> syncCart(@RequestParam Long userId) {
        List<CartDetailDto> dtos = cartService.getCartItemsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }


    // 테스트 샌드박스용 페이지
    @GetMapping("/sandbox")
    public List<CartDto> sandbox() {
        List<CartDto>  target = new ArrayList<>();
        List<CartDetailDto> target1 = new ArrayList<>();
        CartDto dto1 = cartService.getCartDto(1L);
        CartDto dto2 = cartService.getCartDto(2L);
        CartDto dto3 = cartService.getCartDto(3L);
        CartDto dto4 = cartService.getCartDto(4L);
        target.add(dto1);
        target.add(dto2);
        target.add(dto3);
        target.add(dto4);
//        CartDetailDto dto1 = cartService.getCartDetailDto(1L,1L);
//        CartDetailDto dto2 = cartService.getCartDetailDto( 1L,2L);
//        CartDetailDto dto3 = cartService.getCartDetailDto( 1L,3L);
//        CartDetailDto dto4 = cartService.getCartDetailDto( 1L,4L);
//        CartDetailDto dto5 = cartService.getCartDetailDto( 1L,5L);
//        CartDetailDto dto6 = cartService.getCartDetailDto( 1L,6L);
//        CartDetailDto dto7 = cartService.getCartDetailDto( 1L,7L);
//        CartDetailDto dto8 = cartService.getCartDetailDto( 1L,8L);

        return target;
    }
    @PostMapping("/detail")
    public ResponseEntity<List<CartDetailDto>> getCartDetail(@RequestBody List<CartDto> cartDtos) {
        List<CartDetailDto> detailDtos = cartService.getCartDetailsByCartDto(cartDtos);

        return ResponseEntity.status(HttpStatus.OK).body(detailDtos);
    }

}

