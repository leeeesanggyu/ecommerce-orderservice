package com.orderservice.service;

import com.orderservice.domain.dto.OrderDto;
import com.orderservice.domain.entity.OrderEntity;
import com.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderDto createOrder(OrderDto orderDetails) {
        orderDetails.setOrderId(UUID.randomUUID().toString());
        orderDetails.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());
        final OrderEntity orderEntity = modelMapper.map(orderDetails, OrderEntity.class);

        final OrderEntity result = orderRepository.save(orderEntity);
        return modelMapper.map(result, OrderDto.class);
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        final OrderEntity result = orderRepository.findByOrderId(orderId);
        return modelMapper.map(result, OrderDto.class);
    }

    @Override
    public Iterable<OrderEntity> getOrderByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
