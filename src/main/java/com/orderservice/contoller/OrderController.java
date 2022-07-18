package com.orderservice.contoller;

import com.orderservice.domain.OrderReq;
import com.orderservice.domain.dto.OrderDto;
import com.orderservice.domain.dto.OrderRes;
import com.orderservice.domain.entity.OrderEntity;
import com.orderservice.service.producer.KafkaProducer;
import com.orderservice.service.OrderService;
import com.orderservice.service.producer.OrderProducer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @PostMapping(value = "/{userId}/order")
    public ResponseEntity<OrderRes> createOrder(
            @PathVariable("userId") String userId,
            @RequestBody OrderReq orderReq
    ) {
        final OrderDto orderDto = modelMapper.map(orderReq, OrderDto.class);
        orderDto.setUserId(userId);

        /* jpa */
//        final OrderDto result = orderService.createOrder(orderDto);
//        final OrderRes orderRes = modelMapper.map(result, OrderRes.class);

        /* kafka */
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

        kafkaProducer.send("order-topic", orderDto);
        orderProducer.send("orders", orderDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(orderDto, OrderRes.class));
    }

    @GetMapping(value = "/{userId}/order")
    public ResponseEntity<List<OrderRes>> getUserOrder(@PathVariable("userId") String userId) {
        final Iterable<OrderEntity> orderList = orderService.getOrderByUserId(userId);

        final ArrayList<OrderRes> orderResList = new ArrayList<>();
        orderList.forEach(o -> orderResList.add(modelMapper.map(o, OrderRes.class)));
        return ResponseEntity.status(HttpStatus.OK).body(orderResList);
    }
}
