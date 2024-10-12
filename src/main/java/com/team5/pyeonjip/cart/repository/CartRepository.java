package com.team5.pyeonjip.cart.repository;

import com.team5.pyeonjip.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByUserId(Long userId);

    Cart findByUserIdAndOptionId(Long userId, Long optionId);

    void deleteByUserIdAndOptionId(Long userId, Long optionId);

    void deleteAllByUserId(Long userId);
}
