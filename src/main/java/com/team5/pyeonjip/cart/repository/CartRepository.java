package com.team5.pyeonjip.cart.repository;

import com.team5.pyeonjip.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(Long userId);

    @Modifying
    @Transactional
    void deleteByUserIdAndOptionId(Long userId, Long optionId);

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);

    boolean existsByUserIdAndOptionId(Long userId, Long optionId);
}
