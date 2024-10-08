package com.team5.pyeonjip.coupon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String code;    // 쿠폰 코드
    @NotNull
    private Long discount; // 할인율 * 100
    @NotNull
    private boolean active;
    @NotNull
    private LocalDateTime expiryDate; // 만료 날짜

    public Coupon(String code, Long discount, boolean active, LocalDateTime expiryDate) {
        this.code = code;
        this.discount = discount;
        this.active = active;
        this.expiryDate = expiryDate;
    }

}
