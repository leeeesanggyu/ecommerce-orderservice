package com.orderservice.domain.kafkadto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Payload {
    private String order_id;
    private String product_id;
    private String user_id;
    private int qty;
    private int total_price;
    private int unit_price;
}
