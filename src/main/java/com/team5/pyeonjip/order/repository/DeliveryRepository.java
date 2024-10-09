package com.team5.pyeonjip.order.repository;

import com.team5.pyeonjip.order.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
