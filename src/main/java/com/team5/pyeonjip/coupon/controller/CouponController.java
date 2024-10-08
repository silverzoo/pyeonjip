package com.team5.pyeonjip.coupon.controller;

import com.team5.pyeonjip.coupon.entity.Coupon;
import com.team5.pyeonjip.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    // 랜덤 쿠폰 생성 API
    @PostMapping("/create")
    public ResponseEntity<Coupon> createCoupon(@RequestParam Long discount) {
        Coupon coupon = couponService.createRandomCoupon(discount);
        return ResponseEntity.ok(coupon);
    }
}
