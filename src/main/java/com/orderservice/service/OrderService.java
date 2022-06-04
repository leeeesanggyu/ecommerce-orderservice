package com.orderservice.service;

import com.orderservice.domain.dto.OrderDto;
import com.orderservice.domain.entity.OrderEntity;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDetails);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrderByUserId(String userId);
}
