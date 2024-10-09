package com.team5.pyeonjip.order.repository;

import com.team5.pyeonjip.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN User u ON o.user.id = u.id WHERE u.name = :userName")
    List<Order> findOrdersByUserName(@Param("userName") String userName); // 사용자 이름으로 주문 조회

    List<Order> findOrdersByUserId(Long userId); // 사용자 ID로 모든 주문 조회
}
