package com.team5.pyeonjip.cart.repository;

import com.team5.pyeonjip.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByEmail(String email);

    Cart findByEmailAndOptionId(String email, Long optionId);

    void deleteByEmailAndOptionId(String email, Long optionId);

    void deleteAllByEmail(String email);

    void deleteByOptionId(Long optionId);
}
