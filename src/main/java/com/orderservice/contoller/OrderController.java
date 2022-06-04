package com.orderservice.contoller;

import com.orderservice.domain.OrderReq;
import com.orderservice.domain.dto.OrderDto;
import com.orderservice.domain.dto.OrderRes;
import com.orderservice.domain.entity.OrderEntity;
import com.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @PostMapping(value = "/{userId}/order")
    public ResponseEntity<OrderRes> createOrder(
            @PathVariable("userId") String userId,
            @RequestBody OrderReq orderReq
    ) {
        final OrderDto orderDto = modelMapper.map(orderReq, OrderDto.class);
        orderDto.setUserId(userId);
        final OrderDto result = orderService.createOrder(orderDto);
        final OrderRes orderRes = modelMapper.map(result, OrderRes.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderRes);
    }

    @GetMapping(value = "/{userId}/order")
    public ResponseEntity<List<OrderRes>> getUserOrder(@PathVariable("userId") String userId) {
        final Iterable<OrderEntity> orderList = orderService.getOrderByUserId(userId);

        final ArrayList<OrderRes> orderResList = new ArrayList<>();
        orderList.forEach(o -> orderResList.add(modelMapper.map(o, OrderRes.class)));
        return ResponseEntity.status(HttpStatus.OK).body(orderResList);
    }
}
