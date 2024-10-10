package com.team5.pyeonjip.order.repository;

import com.team5.pyeonjip.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
