package com.team5.pyeonjip.order.repository;

import com.team5.pyeonjip.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser_Name(String userName);  // 유저 이름으로 주문 검색
}
