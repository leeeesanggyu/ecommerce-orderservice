package com.orderservice.domain;

import lombok.Data;

@Data
public class OrderReq {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
}
