package com.team5.pyeonjip.order.mapper;

import com.team5.pyeonjip.order.dto.DeliveryRequestDto;
import com.team5.pyeonjip.order.dto.OrderRequestDto;
import com.team5.pyeonjip.order.dto.OrderResponseDto;
import com.team5.pyeonjip.order.entity.Delivery;
import com.team5.pyeonjip.order.entity.Order;
import com.team5.pyeonjip.order.entity.OrderDetail;
import com.team5.pyeonjip.order.enums.DeliveryStatus;
import com.team5.pyeonjip.order.enums.OrderStatus;
import com.team5.pyeonjip.user.entity.User;

import java.util.List;

public class OrderMapper {

    // entity -> dto
    public static OrderResponseDto toDto(Order order, List<OrderDetail> orderDetails) {
        return OrderResponseDto.builder()
                .build();
    }

    // dto -> entity
    public static Order toEntity(OrderRequestDto orderRequestDto, DeliveryRequestDto deliveryRequestDto, User user) {
        Delivery delivery = Delivery.builder()
                .address(deliveryRequestDto.getAddress() != null ? deliveryRequestDto.getAddress() : user.getAddress()) // 배송지 주소, 유저 기본 주소 사용
                .status(DeliveryStatus.READY)             // 기본적으로 배송 상태는 READY
                .build();

        return Order.builder()
                .recipient(orderRequestDto.getRecipient() != null ? orderRequestDto.getRecipient() : user.getName()) // 수령인, 유저 기본 이름 사용
                .phoneNumber(orderRequestDto.getPhoneNum() != null ? orderRequestDto.getPhoneNum() : user.getPhoneNum())   // 연락처, 유저 기본 연락처 사용
                .requirement(orderRequestDto.getRequirement()) // 요청사항은 입력받은 값 사용
                .status(OrderStatus.ORDER)                     // 주문 상태는 기본적으로 ORDER로 설정
                .delivery(delivery)                            // 배송 정보 저장
                .build();
    }
}
