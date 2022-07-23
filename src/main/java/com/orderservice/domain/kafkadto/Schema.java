package com.orderservice.domain.kafkadto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Builder
@Data
public class Schema {
    private String type;
    private List<Field> fields;
    private boolean optional;
    private String name;
}
