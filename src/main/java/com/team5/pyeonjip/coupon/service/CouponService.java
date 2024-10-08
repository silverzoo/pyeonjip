package com.team5.pyeonjip.coupon.service;

import com.team5.pyeonjip.coupon.entity.Coupon;
import com.team5.pyeonjip.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    // 랜덤 쿠폰 생성
    @Transactional
    public Coupon createRandomCoupon(Long discount){
        Coupon coupon = new Coupon();
        coupon.setCode(generateCouponCode()); // 코드 자동 생성
        coupon.setDiscount(discount);
        coupon.setActive(true);
        coupon.setExpiryDate(LocalDateTime.now().plusDays(7)); // 7일간의 유효기간을 가짐

        return saveCoupon(coupon);
    }

    @Transactional
    public Coupon createCoupon(String Code, Long discount, LocalDateTime expiryDate){
        Coupon coupon = new Coupon();
        coupon.setCode(Code);
        coupon.setDiscount(discount);
        coupon.setActive(true);
        coupon.setExpiryDate(expiryDate);

        return saveCoupon(coupon);
    }

    // 쿠폰 코드 자동 생성
    public String generateCouponCode() {
        return UUID.randomUUID().toString().substring(0, 4).toUpperCase(); // 4자리 랜덤 코드 생성
    }

    public Coupon saveCoupon(Coupon coupon) {
        log.info("Created coupon: {}", coupon);
        return couponRepository.save(coupon);
    }
}
