package com.orderservice.service.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.domain.dto.OrderDto;
import com.orderservice.domain.kafkadto.Field;
import com.orderservice.domain.kafkadto.KafkaOrderDto;
import com.orderservice.domain.kafkadto.Payload;
import com.orderservice.domain.kafkadto.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    List<Field> fieldList = Arrays.asList(
            new Field("string", true, "order_id"),
            new Field("string", true, "product_id"),
            new Field("string", true, "user_id"),
            new Field("int32", true, "qty"),
            new Field("int32", true, "total_price"),
            new Field("int32", true, "unit_price")
    );

    Schema schema = Schema.builder()
            .type("struct")
            .field(fieldList)
            .optional(false)
            .name("orders")
            .build();


    public OrderDto send(String kafkaTopic, OrderDto orderDto) {
        final Payload payload = Payload.builder()
                .order_id(orderDto.getOrderId())
                .product_id(orderDto.getProductId())
                .user_id(orderDto.getUserId())
                .qty(orderDto.getQty())
                .total_price(orderDto.getTotalPrice())
                .unit_price(orderDto.getUnitPrice())
                .build();
        final KafkaOrderDto kafkaOrderDto = new KafkaOrderDto(schema, payload);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try {
            jsonInString = mapper.writeValueAsString(kafkaOrderDto);
        } catch (JsonProcessingException e) {
            log.error("kafka send json process error", e);
        }

        kafkaTemplate.send(kafkaTopic, jsonInString);
        log.info("order producer send data from order-service : {}", orderDto);
        return orderDto;
    }
}
