package com.team5.pyeonjip.order.service;

import com.team5.pyeonjip.order.dto.AdminOrderResponseDto;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminOrderService {

    // 주문 수정(배송 상태)
    void updateDeliveryStatus(Long id, DeliveryStatus deliveryStatus);

    // 주문 삭제
    void deleteOrderById(Long orderId);

    // 주문 전체 조회
    Page<AdminOrderResponseDto> findAllOrders(int pageNumber, int size, String sortField, String sortDir, String keyword);
}