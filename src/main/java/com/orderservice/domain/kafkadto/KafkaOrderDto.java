package com.orderservice.domain.kafkadto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
public class KafkaOrderDto implements Serializable {
    private Schema schema;
    private Payload payload;
}
