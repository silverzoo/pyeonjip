package com.team5.pyeonjip.coupon.controller;

import com.team5.pyeonjip.coupon.entity.Coupon;
import com.team5.pyeonjip.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    // 랜덤 쿠폰 생성 API
    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@RequestParam Long discount) {
        Coupon coupon = couponService.createRandomCoupon(discount);
        return ResponseEntity.status(HttpStatus.OK).body(coupon);
    }

    @GetMapping
    public ResponseEntity<List<Coupon>> getCoupons() {
        List<Coupon> coupons = couponService.getAllCoupons();
        return ResponseEntity.status(HttpStatus.OK).body(coupons);
    }

    @DeleteMapping
    public ResponseEntity<Coupon> deleteCoupon(@RequestParam Long id) {
        couponService.deleteCouponById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


