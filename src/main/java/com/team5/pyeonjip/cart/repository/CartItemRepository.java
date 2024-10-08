package com.team5.pyeonjip.cart.repository;

import com.team5.pyeonjip.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
