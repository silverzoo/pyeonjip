package com.team5.pyeonjip.order.controller;

import com.team5.pyeonjip.order.dto.DeliveryRequestDto;
import com.team5.pyeonjip.order.dto.OrderRequestDto;
import com.team5.pyeonjip.order.service.OrderService;
import com.team5.pyeonjip.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderApiController {

    private final OrderService orderService;

    // 주문 생성 페이지
    @GetMapping("/order/create")
    public ResponseEntity<OrderRequestDto> getOrderPage(){
        User user = new User(); // 로그인한 사용자 정보 가져오기 test

        // 기존 배송지
        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .recipient(user.getName())   // 사용자 이름
                .phoneNum(user.getPhoneNumber()) // 사용자 전화번호
                .delivery(new DeliveryRequestDto(user.getAddress())) // 사용자 주소
                .build();

        return ResponseEntity.ok(orderRequestDto);
    }

    // 주문 처리
//    @PostMapping("/order/create")
//    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto, @RequestBody DeliveryRequestDto deliveryRequestDto){
//
//    }
}
