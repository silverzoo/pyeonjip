package com.team5.pyeonjip.cart.repository;

import com.team5.pyeonjip.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.userId = ?1")
    void deleteByUserId(Long userId);
}
