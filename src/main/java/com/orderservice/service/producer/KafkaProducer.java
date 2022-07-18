package com.orderservice.service.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.domain.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderDto send(String kafkaTopic, OrderDto orderDto) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try {
            jsonInString = mapper.writeValueAsString(orderDto);
        } catch (JsonProcessingException e) {
            log.error("kafka send json process error", e);
        }
        kafkaTemplate.send(kafkaTopic, jsonInString);
        log.info("Kafka producer send data from order-service : {}", orderDto);
        return orderDto;
    }
}
