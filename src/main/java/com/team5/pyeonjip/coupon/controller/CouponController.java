package com.team5.pyeonjip.coupon.controller;

import com.team5.pyeonjip.coupon.entity.Coupon;
import com.team5.pyeonjip.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<Coupon>> getCoupons() {
        List<Coupon> coupons = couponService.getAllCoupons();
        return ResponseEntity.status(HttpStatus.OK).body(coupons);
    }
}


